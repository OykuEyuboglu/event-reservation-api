package com.oyku.event_reservation_api.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oyku.event_reservation_api.dto.reservation.ReservationCreateRequest;
import com.oyku.event_reservation_api.dto.reservation.ReservationResponse;
import com.oyku.event_reservation_api.entity.Event;
import com.oyku.event_reservation_api.entity.Reservation;
import com.oyku.event_reservation_api.entity.Seat;
import com.oyku.event_reservation_api.entity.User;
import com.oyku.event_reservation_api.enums.ReservationStatus;
import com.oyku.event_reservation_api.enums.SeatStatus;
import com.oyku.event_reservation_api.exception.BadRequestException;
import com.oyku.event_reservation_api.exception.ConflictException;
import com.oyku.event_reservation_api.exception.ForbiddenException;
import com.oyku.event_reservation_api.exception.ResourceNotFoundException;
import com.oyku.event_reservation_api.mapper.ReservationMapper;
import com.oyku.event_reservation_api.repository.EventRepository;
import com.oyku.event_reservation_api.repository.ReservationRepository;
import com.oyku.event_reservation_api.repository.SeatRepository;
import com.oyku.event_reservation_api.repository.UserRepository;
import com.oyku.event_reservation_api.service.ReservationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

	private final ReservationRepository reservationRepository;
	private final SeatRepository seatRepository;
	private final UserRepository userRepository;
	private final EventRepository eventRepository;
	private final ReservationMapper reservationMapper;

	private static final int MAX_ACTIVE_RESERVATIONS = 5;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReservationServiceImpl.class);

	@Override
	@Transactional
	public ReservationResponse createReservation(ReservationCreateRequest request) {

		Reservation reservation = reservationMapper.toEntity(request);

		try {
			Event event = eventRepository.findById(request.getEventId())
					.orElseThrow(() -> new ResourceNotFoundException("Event does not exist"));

			Seat seat = seatRepository.findById(request.getSeatId())
					.orElseThrow(() -> new ResourceNotFoundException("Seat does not exist"));

			User user = getCurrentUser();

			if (seat.getStatus() != SeatStatus.AVAILABLE) {
				throw new ConflictException("Seat is not available for reservation.");
			}

			long activeReservations = reservationRepository.countByUserAndStatus(user, ReservationStatus.RESERVED);
			
			if (activeReservations >= MAX_ACTIVE_RESERVATIONS) {
				throw new BadRequestException("You can hold a maximum of 5 active reservations at the same time.");
			}

			
			reservation.setSeat(seat);
			reservation.setEvent(event);
			reservation.setUser(user);
			reservation.setStatus(ReservationStatus.RESERVED);
			reservation.setExpiresAt(LocalDateTime.now().plusMinutes(10));

			seat.setStatus(SeatStatus.HELD);
			seatRepository.save(seat);
			LOGGER.info("Seat updated");

			Reservation savedReservation = reservationRepository.save(reservation);
			LOGGER.info("Reservation created");
			return reservationMapper.toResponse(savedReservation);

		} catch (ObjectOptimisticLockingFailureException e) {
			throw new ConflictException("Seat is already reserved");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReservationResponse> getCurrentUserReservations() {

		User user = getCurrentUser();

		List<Reservation> reservations = reservationRepository.findByUser(user);

		return reservationMapper.toResponseList(reservations);
	}

	private User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	@Override
	@Transactional(readOnly = true)
	public ReservationResponse getReservationById(Long id) {

		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Reservation does not exist"));
		
		return reservationMapper.toResponse(reservation);
	}

	@Override
	@Transactional
	public ReservationResponse cancelReservation(Long id) {
		
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Reservation does not exist."));

		User currentUser = getCurrentUser();

		if (!reservation.getUser().getId().equals(currentUser.getId())) {
			throw new ForbiddenException("You are not allowed to cancel this reservation.");
		}

		if (reservation.getStatus() == ReservationStatus.CANCELLED) {
			throw new ConflictException("Reservation is already cancelled.");
		}

		if (reservation.getStatus() == ReservationStatus.EXPIRED) {
			throw new ConflictException("Expired reservation can not be cancelled.");
		}

		Seat seat = reservation.getSeat();

		reservation.setStatus(ReservationStatus.CANCELLED);
		seat.setStatus(SeatStatus.AVAILABLE);

		seatRepository.save(seat);
		reservationRepository.save(reservation);

		LOGGER.info("Reservation cancelled successfully.");

		return reservationMapper.toResponse(reservation);
	}

	@Override
	@Transactional
	public void expireReservations() {

		List<Reservation> expireReservations = reservationRepository
				.findByStatusAndExpiresAtBefore(ReservationStatus.RESERVED, LocalDateTime.now());

		if (expireReservations.isEmpty()) {
			LOGGER.info("No expired reservations found");
			return;
		}

		for (Reservation reservation : expireReservations) {

			reservation.setStatus(ReservationStatus.EXPIRED);

			Seat seat = reservation.getSeat();
			seat.setStatus(SeatStatus.AVAILABLE);
		}

		LOGGER.info("{} reservation(s) expired.", expireReservations.size());
		reservationRepository.saveAll(expireReservations);
	}

	@Override
	@Transactional
	public ReservationResponse confirmReservation(Long id) {
		
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Reservation not found."));

		User currentUser = getCurrentUser();

		if (!reservation.getUser().getId().equals(currentUser.getId())) {
			throw new ForbiddenException("You are not allowed to confirm this reservation.");
		}

		if (reservation.getStatus() != ReservationStatus.RESERVED) {
			throw new ConflictException("Only held reservations can be confirmed.");
		}

		Seat seat = reservation.getSeat();

		reservation.setStatus(ReservationStatus.CONFIRMED);
		seat.setStatus(SeatStatus.SOLD);

		seatRepository.save(seat);
		reservationRepository.save(reservation);

		LOGGER.info("Reservation confirmed successfully.");
		
		return reservationMapper.toResponse(reservation);
	}
}

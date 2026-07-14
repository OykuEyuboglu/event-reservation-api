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
import com.oyku.event_reservation_api.exception.ConflictException;
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

	private static final Logger log =
	        LoggerFactory.getLogger(ReservationServiceImpl.class);
	
	@Override
	@Transactional
	public ReservationResponse createReservation(ReservationCreateRequest request) {

		log.info("Create reservation started");
		
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

		reservation.setSeat(seat);
		reservation.setEvent(event);
		reservation.setUser(user);
		reservation.setStatus(ReservationStatus.RESERVED);
		reservation.setExpiresAt(LocalDateTime.now().plusMinutes(10));

		seat.setStatus(SeatStatus.HELD);
		seatRepository.save(seat);
		log.info("Seat updated");

		Reservation savedReservation = reservationRepository.save(reservation);
		log.info("Reservation created");
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

		Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation does not exist"));
		
		return reservationMapper.toResponse(reservation);
	}

	@Override
	@Transactional
	public void cancelReservation(Long id) {

		Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation does not exist"));
		
		Seat seat = reservation.getSeat();
		seat.setStatus(SeatStatus.AVAILABLE);
		
		reservation.setStatus(ReservationStatus.CANCELLED);
		reservationRepository.save(reservation);
	}
}

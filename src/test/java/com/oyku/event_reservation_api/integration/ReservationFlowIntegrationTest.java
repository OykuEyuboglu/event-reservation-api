package com.oyku.event_reservation_api.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.oyku.event_reservation_api.dto.reservation.ReservationCreateRequest;
import com.oyku.event_reservation_api.dto.reservation.ReservationResponse;
import com.oyku.event_reservation_api.entity.Event;
import com.oyku.event_reservation_api.entity.Reservation;
import com.oyku.event_reservation_api.entity.Seat;
import com.oyku.event_reservation_api.entity.User;
import com.oyku.event_reservation_api.enums.ReservationStatus;
import com.oyku.event_reservation_api.enums.Role;
import com.oyku.event_reservation_api.enums.SeatStatus;
import com.oyku.event_reservation_api.repository.EventRepository;
import com.oyku.event_reservation_api.repository.ReservationRepository;
import com.oyku.event_reservation_api.repository.SeatRepository;
import com.oyku.event_reservation_api.repository.UserRepository;
import com.oyku.event_reservation_api.service.impl.ReservationServiceImpl;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class ReservationFlowIntegrationTest {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private SeatRepository seatRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private ReservationServiceImpl reservationServiceImpl;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	private static final String EMAIL = "email33@gmail.com";
	
	@Test
	void shouldCompleteReservationFlowSuccessfully() {

		Event event = createEvent();
		event = eventRepository.save(event);

		Seat seat = createSeat();
		seat.setEvent(event);
		seat = seatRepository.save(seat);

		User user = createUser();
		user = userRepository.save(user);
		
		ReservationCreateRequest request = new ReservationCreateRequest();
		request.setEventId(event.getId());
		request.setSeatId(seat.getId());


		AddSecurity();

		ReservationResponse reservation = reservationServiceImpl.createReservation(request);

		assertNotNull(reservation);
		assertNotNull(request.getEventId());
		assertNotNull(request.getSeatId());
		assertEquals(ReservationStatus.RESERVED, reservation.getStatus());
		assertEquals(
		        EMAIL,
		        SecurityContextHolder.getContext().getAuthentication().getName());
		
		Seat heldSeat = seatRepository.findById(seat.getId()).orElseThrow();
		assertEquals(SeatStatus.HELD, heldSeat.getStatus());

		ReservationResponse confirmed =
		        reservationServiceImpl.confirmReservation(
		                reservation.getId());
		
		assertEquals(ReservationStatus.CONFIRMED, confirmed.getStatus());
		
		Reservation saved =
		        reservationRepository.findById(reservation.getId()).orElseThrow();
		
		assertEquals(ReservationStatus.CONFIRMED, saved.getStatus());
		
		Seat soldSeat =
		        seatRepository.findById(seat.getId()).orElseThrow();

		assertEquals(SeatStatus.SOLD, soldSeat.getStatus());
		
	}

	private SecurityContext AddSecurity() {

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(new UsernamePasswordAuthenticationToken(EMAIL, null, List.of(() -> "ROLE_USER")));

		SecurityContextHolder.setContext(context);

		return context;
	}

	private User createUser() {
		User user = new User();
		user.setEmail(EMAIL);
		user.setName("Test User");
		user.setPasswordHash(passwordEncoder.encode("password"));
		user.setRole(Set.of(Role.USER));
		return user;
	}

	private Seat createSeat() {
		Seat seat = new Seat();
		seat.setSeatNumber("A1");
		seat.setStatus(SeatStatus.AVAILABLE);
		seat.setVersion(0L);
		return seat;
	}

	private Event createEvent() {
		Event event = new Event();
		event.setName("Concert");
		event.setAddress("Ankara");
		event.setLocation("Arena");
		event.setTicketPrice(BigDecimal.valueOf(500));
		event.setEventDate(LocalDateTime.now().plusDays(5));
		return event;
	}
}

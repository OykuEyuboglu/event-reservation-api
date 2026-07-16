package com.oyku.event_reservation_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.oyku.event_reservation_api.dto.reservation.ReservationCreateRequest;
import com.oyku.event_reservation_api.dto.reservation.ReservationResponse;
import com.oyku.event_reservation_api.entity.Event;
import com.oyku.event_reservation_api.entity.Reservation;
import com.oyku.event_reservation_api.entity.Seat;
import com.oyku.event_reservation_api.entity.User;
import com.oyku.event_reservation_api.enums.ReservationStatus;
import com.oyku.event_reservation_api.enums.Role;
import com.oyku.event_reservation_api.enums.SeatStatus;
import com.oyku.event_reservation_api.mapper.ReservationMapper;
import com.oyku.event_reservation_api.repository.EventRepository;
import com.oyku.event_reservation_api.repository.ReservationRepository;
import com.oyku.event_reservation_api.repository.SeatRepository;
import com.oyku.event_reservation_api.repository.UserRepository;
import com.oyku.event_reservation_api.service.impl.ReservationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {

	@Mock
	private ReservationRepository reservationRepository;

	@Mock
	private SeatRepository seatRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private EventRepository eventRepository;

	@Mock
	private ReservationMapper reservationMapper;

	@InjectMocks
	private ReservationServiceImpl reservationServiceImpl;


	private static final String EMAİL = "email22@gmail.com";
	private static final Long SEAT_ID = 29L;
	private static final Long EVENT_ID = 2L;
	private static final String SEAT_NUMBER = "A1";
	private static final LocalDateTime EXPIRES_AT = LocalDateTime.now().plusMinutes(10);
	
	@Test
	void shouldCreateReservationSuccessfully() {

		ReservationCreateRequest request = createRequest();

		Event event = createEvent();
		Seat seat = createSeat();
		User user = createUser();
		Reservation reservation = createReservation();
		ReservationResponse response = createResponse();

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(
				new UsernamePasswordAuthenticationToken("email22@gmail.com", null, List.of(() -> "ROLE_USER")));

		SecurityContextHolder.setContext(context);

		when(eventRepository.findById(request.getEventId())).thenReturn(Optional.of(event));

		when(seatRepository.findById(request.getSeatId())).thenReturn(Optional.of(seat));

		when(userRepository.findByEmail("email22@gmail.com")).thenReturn(Optional.of(user));

		when(seatRepository.save(any(Seat.class))).thenReturn(seat);

		when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
		when(reservationMapper.toEntity(request)).thenReturn(reservation);
		when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(response);

		ReservationResponse result = reservationServiceImpl.createReservation(request);
		assertNotNull(response);

		assertEquals(ReservationStatus.RESERVED, result.getStatus());
		assertEquals(request.getSeatId(), result.getSeatId());
		assertEquals(request.getEventId(), result.getEventId());
		verify(eventRepository).findById(request.getEventId());

		verify(seatRepository).findById(request.getSeatId());

		verify(userRepository).findByEmail("email22@gmail.com");

		verify(seatRepository).save(any(Seat.class));

		verify(reservationRepository).save(any(Reservation.class));

		verify(reservationMapper).toResponse(any(Reservation.class));
	}

	@Test
	void shouldCurrentReservationSuccessfully() {

		User user = createUser();
		Reservation reservation = createReservation();
		ReservationResponse response = createResponse();

		List<Reservation> reservations = List.of(reservation);
		List<ReservationResponse> responses = List.of(response);

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(
				new UsernamePasswordAuthenticationToken("email22@gmail.com", null, List.of(() -> "ROLE_USER")));

		SecurityContextHolder.setContext(context);
		when(userRepository.findByEmail("email22@gmail.com")).thenReturn(Optional.of(user));

		when(reservationRepository.findByUser(user)).thenReturn(reservations);

		when(reservationMapper.toResponseList(reservations)).thenReturn(responses);

		List<ReservationResponse> result = reservationServiceImpl.getCurrentUserReservations();

		assertNotNull(response);
		assertEquals(1, result.size());
		assertEquals(ReservationStatus.RESERVED, result.get(0).getStatus());

		assertNotNull(result);

		assertEquals(1, result.size());

		assertEquals(ReservationStatus.RESERVED, result.get(0).getStatus());

		assertEquals("Concert", result.get(0).getEventName());

		verify(userRepository).findByEmail("email22@gmail.com");

		verify(reservationRepository).findByUser(user);

		verify(reservationMapper).toResponseList(reservations);
	}

	private ReservationCreateRequest createRequest() {
		ReservationCreateRequest request = new ReservationCreateRequest();
		request.setEventId(2L);
		request.setSeatId(29L);
		return request;
	}

	private Reservation createReservation() {
		Reservation reservation = new Reservation();
		reservation.setId(1L);
		reservation.setStatus(ReservationStatus.RESERVED);
		reservation.setExpiresAt(EXPIRES_AT);
		reservation.setSeat(createSeat());
		reservation.setEvent(createEvent());
		reservation.setUser(createUser());
		return reservation;
	}

	private User createUser() {
		User user = new User();
		user.setId(1L);
		user.setEmail(EMAİL);
		user.setName("Test User");
		user.setPasswordHash("password2");
		user.setRole(Set.of(Role.USER));
		return user;
	}

	private Seat createSeat() {
		Seat seat = new Seat();
		seat.setId(SEAT_ID);
		seat.setSeatNumber(SEAT_NUMBER);
		seat.setStatus(SeatStatus.AVAILABLE);
		seat.setVersion(0L);
		return seat;
	}

	private Event createEvent() {
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setName("Concert");
		event.setAddress("Ankara");
		event.setLocation("Arena");
		event.setTicketPrice(BigDecimal.valueOf(500));
		event.setEventDate(EXPIRES_AT);
		return event;
	}

	private ReservationResponse createResponse() {
		ReservationResponse response = new ReservationResponse();
		response.setId(1L);
		response.setEventId(EVENT_ID);
		response.setSeatId(SEAT_ID);
		response.setSeatNumber(SEAT_NUMBER);
		response.setStatus(ReservationStatus.RESERVED);
		response.setEventName("Concert");
		response.setExpiresAt(EXPIRES_AT);
		return response;
	}
}

package com.oyku.event_reservation_api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
import com.oyku.event_reservation_api.exception.ConflictException;
import com.oyku.event_reservation_api.exception.ResourceNotFoundException;
import com.oyku.event_reservation_api.mapper.ReservationMapper;
import com.oyku.event_reservation_api.repository.EventRepository;
import com.oyku.event_reservation_api.repository.ReservationRepository;
import com.oyku.event_reservation_api.repository.SeatRepository;
import com.oyku.event_reservation_api.repository.UserRepository;
import com.oyku.event_reservation_api.service.impl.ReservationServiceImpl;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

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

    private static final String EMAIL = "email22@gmail.com";
    private static final Long EVENT_ID = 2L;
    private static final Long SEAT_ID = 29L;
    private static final Long RESERVATION_ID = 1L;
    private static final LocalDateTime EXPIRES_AT = LocalDateTime.now().plusMinutes(10);

    @Test
    void shouldCreateReservationSuccessfully() {

        ReservationCreateRequest request = createRequest();
        Event event = createEvent();
        Seat seat = createSeat();
        User user = createUser();
        Reservation reservation = createReservation();
        ReservationResponse response = createResponse();

        AddSecurity();

        when(reservationMapper.toEntity(request)).thenReturn(reservation);
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));
        when(seatRepository.findById(SEAT_ID)).thenReturn(Optional.of(seat));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(seatRepository.save(any())).thenReturn(seat);
        when(reservationRepository.save(any())).thenReturn(reservation);
        when(reservationMapper.toResponse(any())).thenReturn(response);

        ReservationResponse result =
                reservationServiceImpl.createReservation(request);

        assertNotNull(result);
        assertEquals(ReservationStatus.RESERVED, result.getStatus());
        assertEquals(EVENT_ID, result.getEventId());
        assertEquals(SEAT_ID, result.getSeatId());

        verify(eventRepository).findById(EVENT_ID);
        verify(seatRepository).findById(SEAT_ID);
        verify(userRepository).findByEmail(EMAIL);
        verify(seatRepository).save(any());
        verify(reservationRepository).save(any());
        verify(reservationMapper).toResponse(any());
    }

    @Test
    void shouldThrowWhenSeatNotAvailable() {

        ReservationCreateRequest request = createRequest();
        Reservation reservation = createReservation();
        Event event = createEvent();
        Seat seat = createSeat();
        User user = createUser();

        seat.setStatus(SeatStatus.HELD);

       AddSecurity();

        when(reservationMapper.toEntity(request)).thenReturn(reservation);
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));
        when(seatRepository.findById(SEAT_ID)).thenReturn(Optional.of(seat));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        assertThrows(
                ConflictException.class,
                () -> reservationServiceImpl.createReservation(request));

        verify(reservationRepository, never()).save(any());
    }
    
    
    
    @Test
    void shouldGetCurrentUserReservationsSuccessfully() {

        User user = createUser();
        Reservation reservation = createReservation();
        ReservationResponse response = createResponse();

        List<Reservation> reservations = List.of(reservation);
        List<ReservationResponse> responses = List.of(response);

        AddSecurity();

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(reservationRepository.findByUser(user)).thenReturn(reservations);
        when(reservationMapper.toResponseList(reservations)).thenReturn(responses);

        List<ReservationResponse> result =
                reservationServiceImpl.getCurrentUserReservations();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ReservationStatus.RESERVED, result.get(0).getStatus());

        verify(userRepository).findByEmail(EMAIL);
        verify(reservationRepository).findByUser(user);
        verify(reservationMapper).toResponseList(reservations);
    }

    @Test
    void shouldGetReservationByIdSuccessfully() {

        Reservation reservation = createReservation();
        ReservationResponse response = createResponse();

        when(reservationRepository.findById(RESERVATION_ID))
                .thenReturn(Optional.of(reservation));

        when(reservationMapper.toResponse(reservation))
                .thenReturn(response);

        ReservationResponse result =
                reservationServiceImpl.getReservationById(RESERVATION_ID);

        assertNotNull(result);
        assertEquals(RESERVATION_ID, result.getId());

        verify(reservationRepository).findById(RESERVATION_ID);
        verify(reservationMapper).toResponse(reservation);
    }

    @Test
    void shouldThrowWhenReservationNotFound() {

        when(reservationRepository.findById(RESERVATION_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reservationServiceImpl.getReservationById(RESERVATION_ID));

        verify(reservationRepository).findById(RESERVATION_ID);
        verify(reservationMapper, never()).toResponse(any());
    }

    @Test
    void shouldConfirmReservationSuccessfully() {

        User user = createUser();
        Reservation reservation = createReservation();
        ReservationResponse response = createResponse();
        response.setStatus(ReservationStatus.CONFIRMED);
        
        AddSecurity();

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(reservationRepository.findById(RESERVATION_ID))
                .thenReturn(Optional.of(reservation));

        when(seatRepository.save(any())).thenReturn(reservation.getSeat());
        when(reservationRepository.save(any())).thenReturn(reservation);
        when(reservationMapper.toResponse(any())).thenReturn(response);

        ReservationResponse result =
                reservationServiceImpl.confirmReservation(RESERVATION_ID);

        assertNotNull(result);
        assertEquals(ReservationStatus.CONFIRMED, result.getStatus());
        assertEquals(RESERVATION_ID, result.getId());
        assertEquals(ReservationStatus.CONFIRMED,
                reservation.getStatus());

        assertEquals(SeatStatus.SOLD,
                reservation.getSeat().getStatus());

        verify(seatRepository).save(any());
        verify(reservationRepository).save(any());
    }
    
    
    @Test
    void shouldCancelReservationSuccessfully() {

        User user = createUser();
        Reservation reservation = createReservation();
        ReservationResponse response = createResponse();

        AddSecurity();

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(reservationRepository.findById(RESERVATION_ID))
                .thenReturn(Optional.of(reservation));

        when(seatRepository.save(any())).thenReturn(reservation.getSeat());
        when(reservationRepository.save(any())).thenReturn(reservation);
        when(reservationMapper.toResponse(any())).thenReturn(response);

        ReservationResponse result =
                reservationServiceImpl.cancelReservation(RESERVATION_ID);

        assertNotNull(result);
        assertEquals(ReservationStatus.CANCELLED,
                reservation.getStatus());

        assertEquals(SeatStatus.AVAILABLE,
                reservation.getSeat().getStatus());

        verify(seatRepository).save(any());
        verify(reservationRepository).save(any());
    }

    @Test
    void shouldExpireReservationsSuccessfully() {

        Reservation reservation = createReservation();
        reservation.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        when(reservationRepository.findByStatusAndExpiresAtBefore(
                eq(ReservationStatus.RESERVED),
                any(LocalDateTime.class)))
                .thenReturn(List.of(reservation));

        reservationServiceImpl.expireReservations();

        assertEquals(ReservationStatus.EXPIRED,
                reservation.getStatus());

        assertEquals(SeatStatus.AVAILABLE,
                reservation.getSeat().getStatus());

        verify(reservationRepository).saveAll(any());
    }
    
    @Test
    void shouldReserveThenConfirmSeatSuccessfully() {

        ReservationCreateRequest request = createRequest();
        Event event = createEvent();
        Seat seat = createSeat();
        User user = createUser();
        Reservation reservation = createReservation();

        AddSecurity();

        when(reservationMapper.toEntity(request)).thenReturn(reservation);
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));
        when(seatRepository.findById(SEAT_ID)).thenReturn(Optional.of(seat));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(seatRepository.save(any())).thenReturn(seat);
        when(reservationRepository.save(any())).thenReturn(reservation);

        reservationServiceImpl.createReservation(request);

        assertEquals(
                ReservationStatus.RESERVED,
                reservation.getStatus());

        assertEquals(
                SeatStatus.HELD,
                reservation.getSeat().getStatus());

        when(reservationRepository.findById(RESERVATION_ID))
                .thenReturn(Optional.of(reservation));

        reservationServiceImpl.confirmReservation(RESERVATION_ID);

        assertEquals(
                ReservationStatus.CONFIRMED,
                reservation.getStatus());

        assertEquals(
                SeatStatus.SOLD,
                reservation.getSeat().getStatus());
    }

    private SecurityContext AddSecurity() {
    	
    	 SecurityContext context = SecurityContextHolder.createEmptyContext();
         context.setAuthentication(
                 new UsernamePasswordAuthenticationToken(
                         EMAIL,
                         null,
                         List.of(() -> "ROLE_USER")));

         SecurityContextHolder.setContext(context);
         
         return context;
    }
    
    private ReservationCreateRequest createRequest() {
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setEventId(EVENT_ID);
        request.setSeatId(SEAT_ID);
        return request;
    }

    private Reservation createReservation() {
        Reservation reservation = new Reservation();
        reservation.setId(RESERVATION_ID);
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
        user.setEmail(EMAIL);
        user.setName("Test User");
        user.setPasswordHash("password");
        user.setRole(Set.of(Role.USER));
        return user;
    }

    private Seat createSeat() {
        Seat seat = new Seat();
        seat.setId(SEAT_ID);
        seat.setSeatNumber("A1");
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
        event.setEventDate(LocalDateTime.now().plusDays(5));
        return event;
    }

    private ReservationResponse createResponse() {
        ReservationResponse response = new ReservationResponse();
        response.setId(RESERVATION_ID);
        response.setEventId(EVENT_ID);
        response.setSeatId(SEAT_ID);
        response.setSeatNumber("A1");
        response.setEventName("Concert");
        response.setStatus(ReservationStatus.RESERVED);
        response.setExpiresAt(EXPIRES_AT);
        return response;
    }
}
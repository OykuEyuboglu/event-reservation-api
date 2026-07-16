package com.oyku.event_reservation_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oyku.event_reservation_api.dto.event.EventCreateRequest;
import com.oyku.event_reservation_api.dto.event.EventResponse;
import com.oyku.event_reservation_api.dto.event.EventUpdateRequest;
import com.oyku.event_reservation_api.dto.seat.SeatResponse;
import com.oyku.event_reservation_api.entity.Event;
import com.oyku.event_reservation_api.entity.Seat;
import com.oyku.event_reservation_api.enums.SeatStatus;
import com.oyku.event_reservation_api.exception.ResourceNotFoundException;
import com.oyku.event_reservation_api.mapper.EventMapper;
import com.oyku.event_reservation_api.mapper.SeatMapperImpl;
import com.oyku.event_reservation_api.repository.EventRepository;
import com.oyku.event_reservation_api.service.impl.EventServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {

	@Mock
	private SeatMapperImpl seatMapperImpl;

	@Mock
	private EventRepository eventRepository;

	@Mock
	private EventMapper eventMapper;

	@InjectMocks
	private EventServiceImpl eventServiceImpl;

	private static final String LOCATION = "Test Location";
	private static final String NAME = "Test Name";
	private static final Long EVENT_ID = 2L;
	private static final LocalDateTime EVENT_DATE = LocalDateTime.now().plusDays(5);
	private static final BigDecimal TICKET_PRICE = BigDecimal.valueOf(500);
	private static final Long SEAT_ID = 29L;
	private static final String SEAT_NUMBER = "A1";

	@Test
	void shouldCreateEventSuccessfully() {

		EventCreateRequest request = createRequest();
		Event event = createEvent();
		EventResponse response = createResponse();

		when(eventMapper.toEntity(request)).thenReturn(event);
		when(eventRepository.save(any(Event.class))).thenReturn(event);
		when(eventMapper.toResponse(event)).thenReturn(response);

		EventResponse result = eventServiceImpl.createEvent(request);

		assertNotNull(result);
		assertEquals(request.getName(), result.getName());

		verify(eventMapper).toEntity(request);
		verify(eventRepository).save(any(Event.class));
		verify(eventMapper).toResponse(event);
	}

	@Test
	void shouldReturnAllEventsSuccessfully() {
		Event event = createEvent();
		EventResponse response = createResponse();

		when(eventRepository.findAll()).thenReturn(List.of(event));
		when(eventMapper.toResponseList(List.of(event))).thenReturn(List.of(response));

		List<EventResponse> result = eventServiceImpl.getAllEvents();

		assertEquals(1, result.size());
		assertEquals(response.getName(), result.get(0).getName());

		verify(eventRepository).findAll();
		verify(eventMapper).toResponseList(List.of(event));
	}

	@Test
	void shouldReturnEventByIdSuccessfully() {

		Event event = createEvent();
		EventResponse response = createResponse();

		when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(eventMapper.toResponse(event)).thenReturn(response);

		EventResponse result = eventServiceImpl.getEventById(EVENT_ID);

		assertNotNull(result);
		assertEquals(response.getName(), result.getName());

		verify(eventRepository).findById(EVENT_ID);
		verify(eventMapper).toResponse(event);
	}

	@Test
	void shouldThrowExceptionWhenEventNotFound() {

		when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> eventServiceImpl.getEventById(EVENT_ID));

		assertEquals("Event does not exist.", exception.getMessage());

		verify(eventRepository).findById(EVENT_ID);
		verify(eventMapper, never()).toResponse(any());
	}

	@Test
	void shouldUpdateEventSuccessfully() {

		EventUpdateRequest request = new EventUpdateRequest();

		request.setName(NAME);

		Event event = createEvent();
		EventResponse response = createResponse();
		response.setName(NAME);

		when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));

		when(eventRepository.save(event)).thenReturn(event);

		when(eventMapper.toResponse(event)).thenReturn(response);

		EventResponse result = eventServiceImpl.updateEvent(EVENT_ID, request);

		assertNotNull(result);
		assertEquals(response.getName(), result.getName());

		verify(eventRepository).findById(EVENT_ID);

		verify(eventMapper).updateEventFromRequest(request, event);

		verify(eventRepository).save(event);
		verify(eventMapper).toResponse(event);
	}

	@Test
	void shouldThrowExceptionWhenUpdatingNonExistingEvent() {

		EventUpdateRequest request = new EventUpdateRequest();

		request.setName(NAME);

		when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> eventServiceImpl.updateEvent(EVENT_ID, request));

		assertEquals("Event does not exist.", exception.getMessage());

		verify(eventRepository).findById(EVENT_ID);
		verify(eventMapper, never()).updateEventFromRequest(any(), any());
		verify(eventRepository, never()).save(any(Event.class));
	}

	@Test
	void shouldDeleteEventSuccessfully() {

		Event event = createEvent();

		when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));

		eventServiceImpl.deleteEvents(EVENT_ID);

		verify(eventRepository).findById(EVENT_ID);
		verify(eventRepository).delete(event);
	}

	@Test
	void shouldThrowExceptionWhenDeletingNonExistingEvent() {

		when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> eventServiceImpl.deleteEvents(EVENT_ID));

		assertEquals("Event does not exist.", exception.getMessage());

		verify(eventRepository).findById(EVENT_ID);
		verify(eventRepository, never()).delete(any());
	}

	@Test
	void shouldReturnAllSeatsByEventIdSuccessfully() {

		Event event = createEvent();
		Seat seat = createSeat();
		SeatResponse response = createSeatResponse();
		event.setSeats(List.of(seat));

		when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(seatMapperImpl.toResponseList(List.of(seat))).thenReturn(List.of(response));

		List<SeatResponse> result = eventServiceImpl.getAllSeatsByEventId(EVENT_ID);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(response.getSeatNumber(), result.get(0).getSeatNumber());

		verify(eventRepository).findById(EVENT_ID);
		verify(seatMapperImpl).toResponseList(List.of(seat));
	}

	private Event createEvent() {
		Event event = new Event();
		event.setName(NAME);
		event.setAddress("Test Address");
		event.setEventDate(EVENT_DATE);
		event.setId(EVENT_ID);
		event.setLocation(LOCATION);
		event.setTicketPrice(TICKET_PRICE);

		return event;
	}

	private EventCreateRequest createRequest() {
		EventCreateRequest request = new EventCreateRequest();
		request.setName(NAME);
		request.setAddress("Test Address");
		request.setEventDate(EVENT_DATE);
		request.setLocation(LOCATION);
		request.setSeatCount(20);
		request.setTicketPrice(TICKET_PRICE);
		return request;
	}

	private EventResponse createResponse() {
		EventResponse response = new EventResponse();
		response.setId(EVENT_ID);
		response.setName(NAME);
		response.setAddress("Test Address");
		response.setLocation(LOCATION);
		response.setTicketPrice(TICKET_PRICE);
		response.setEventDate(EVENT_DATE);
		return response;
	}

	private Seat createSeat() {
		Seat seat = new Seat();
		seat.setId(SEAT_ID);
		seat.setSeatNumber(SEAT_NUMBER);
		seat.setStatus(SeatStatus.AVAILABLE);
		seat.setVersion(0L);
		return seat;
	}

	private SeatResponse createSeatResponse() {
		SeatResponse response = new SeatResponse();
		response.setId(SEAT_ID);
		response.setSeatNumber(SEAT_NUMBER);
		response.setStatus(SeatStatus.AVAILABLE);
		return response;
	}
}

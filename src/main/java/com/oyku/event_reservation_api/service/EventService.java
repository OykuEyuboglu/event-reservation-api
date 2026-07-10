package com.oyku.event_reservation_api.service;

import java.util.List;

import com.oyku.event_reservation_api.dto.event.EventCreateRequest;
import com.oyku.event_reservation_api.dto.event.EventResponse;
import com.oyku.event_reservation_api.dto.event.EventUpdateRequest;
import com.oyku.event_reservation_api.dto.seat.SeatResponse;
import com.oyku.event_reservation_api.entity.Event;

public interface EventService {

	EventResponse createEvent(EventCreateRequest request);

	List<EventResponse> getAllEvents();

	EventResponse getEventById(Long id);
	
	EventResponse updateEvent(Long id, EventUpdateRequest request);
	
	void deleteEvents(Long id);

	Event findEventbyIdOrThrow(Long id);

	List<SeatResponse> getAllSeatsByEventId(Long id);
}

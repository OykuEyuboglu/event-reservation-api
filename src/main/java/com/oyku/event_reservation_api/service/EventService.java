package com.oyku.event_reservation_api.service;

import java.util.List;

import com.oyku.event_reservation_api.dto.event.EventCreateRequest;
import com.oyku.event_reservation_api.dto.event.EventResponse;
import com.oyku.event_reservation_api.dto.event.EventUpdateRequest;

public interface EventService {

	EventResponse createEvent(EventCreateRequest request);

	List<EventResponse> getAllEvents();

	EventResponse getEventById(Long id);
	
	EventResponse updateEvent(Long id, EventUpdateRequest request);
	
	void deleteEvents(Long id);
}

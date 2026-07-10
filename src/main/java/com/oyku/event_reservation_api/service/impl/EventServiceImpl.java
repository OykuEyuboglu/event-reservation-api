package com.oyku.event_reservation_api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oyku.event_reservation_api.dto.event.EventCreateRequest;
import com.oyku.event_reservation_api.dto.event.EventResponse;
import com.oyku.event_reservation_api.dto.event.EventUpdateRequest;
import com.oyku.event_reservation_api.mapper.EventMapper;
import com.oyku.event_reservation_api.repository.EventRepository;
import com.oyku.event_reservation_api.repository.UserRepository;
import com.oyku.event_reservation_api.service.EventService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{
	
	private final EventRepository eventRepository;
	private final UserRepository userRepository;
	private final EventMapper eventMapper;
	
	@Override
	@Transactional
	public EventResponse createEvent(EventCreateRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventResponse> getAllEvents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public EventResponse getEventById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public EventResponse updateEvent(Long id, EventUpdateRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void deleteEvents(Long id) {
		// TODO Auto-generated method stub
		
	}

}

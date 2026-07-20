package com.oyku.event_reservation_api.service.impl;

import com.oyku.event_reservation_api.mapper.SeatMapperImpl;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oyku.event_reservation_api.dto.event.EventCreateRequest;
import com.oyku.event_reservation_api.dto.event.EventResponse;
import com.oyku.event_reservation_api.dto.event.EventUpdateRequest;
import com.oyku.event_reservation_api.dto.seat.SeatResponse;
import com.oyku.event_reservation_api.entity.Event;
import com.oyku.event_reservation_api.entity.Seat;
import com.oyku.event_reservation_api.enums.SeatStatus;
import com.oyku.event_reservation_api.exception.ResourceNotFoundException;
import com.oyku.event_reservation_api.mapper.EventMapper;
import com.oyku.event_reservation_api.repository.EventRepository;
import com.oyku.event_reservation_api.service.AuthService;
import com.oyku.event_reservation_api.service.EventService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

	private final SeatMapperImpl seatMapperImpl;
	private final EventRepository eventRepository;
	private final EventMapper eventMapper;
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

	@Override
	@CacheEvict(value = "events", allEntries = true)
	@Transactional
	public EventResponse createEvent(EventCreateRequest request) {

		Event event = eventMapper.toEntity(request);
		List<Seat> seats = new ArrayList<>();

		char row = 'A';
		int seatNumber = 1;

		for (int i = 1; i <= request.getSeatCount(); i++) {

			Seat seat = new Seat();
			seat.setSeatNumber(row + String.valueOf(seatNumber));
			seat.setStatus(SeatStatus.AVAILABLE);
			seat.setEvent(event);
			seats.add(seat);

			seatNumber++;

			if (seatNumber > 20) {
				seatNumber = 1;
				row++;
			}
		}
		event.setSeats(seats);

		Event savedEvent = eventRepository.save(event);
		return eventMapper.toResponse(savedEvent);

	}

	@Override
	@Cacheable("events")
	@Transactional(readOnly = true)
	public List<EventResponse> getAllEvents() {

		LOGGER.info("Database Called.");
		List<Event> events = eventRepository.findAll();

		return eventMapper.toResponseList(events);
	}

	@Override
	@Cacheable(value = "event", key = "#id")
	@Transactional(readOnly = true)
	public EventResponse getEventById(Long id) {

		Event event = findEventbyIdOrThrow(id);
		return eventMapper.toResponse(event);

	}

	@Override
	@Caching(put = {
			@CachePut(value = "event", key = "#id") }, evict = { @CacheEvict(value = "events", allEntries = true) })
	@Transactional
	public EventResponse updateEvent(Long id, EventUpdateRequest request) {

		Event event = findEventbyIdOrThrow(id);

		eventMapper.updateEventFromRequest(request, event);

		Event updatedEvent = eventRepository.save(event);

		return eventMapper.toResponse(updatedEvent);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "event", key = "#id"), @CacheEvict(value = "events", allEntries = true) })

	@Transactional
	public void deleteEvents(Long id) {
		Event event = findEventbyIdOrThrow(id);
		eventRepository.delete(event);

	}

	@Override
	@Transactional(readOnly = true)
	public List<SeatResponse> getAllSeatsByEventId(Long id) {

		Event event = findEventbyIdOrThrow(id);

		return seatMapperImpl.toResponseList(event.getSeats());
	}

	@Override
	public Event findEventbyIdOrThrow(Long id) {
		return eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event does not exist."));
	}

}

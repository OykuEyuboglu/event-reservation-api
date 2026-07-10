package com.oyku.event_reservation_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oyku.event_reservation_api.dto.event.EventCreateRequest;
import com.oyku.event_reservation_api.dto.event.EventResponse;
import com.oyku.event_reservation_api.dto.event.EventUpdateRequest;
import com.oyku.event_reservation_api.dto.seat.SeatResponse;
import com.oyku.event_reservation_api.service.EventService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

	private final EventService eventService;
	
	@PostMapping
	public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventCreateRequest eventCreateRequest){
		
		EventResponse createdEvent = eventService.createEvent(eventCreateRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
	}
	
	@GetMapping
	public ResponseEntity<List<EventResponse>> getAllEvents(){
		
		List<EventResponse> events = eventService.getAllEvents();
		return ResponseEntity.ok(events);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EventResponse> getPostById(@PathVariable Long id) {

		EventResponse post = eventService.getEventById(id);

		return ResponseEntity.ok(post);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<EventResponse> updatedEvent(@PathVariable Long id, @Valid @RequestBody EventUpdateRequest eventUpdateRequest){
		
		EventResponse updatedEvent = eventService.updateEvent(id, eventUpdateRequest);
		return ResponseEntity.ok(updatedEvent);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEvent(@PathVariable Long id){
		eventService.deleteEvents(id);
		return ResponseEntity.noContent().build();
	}
	

	@GetMapping("/{id}/seats")
	public ResponseEntity<List<SeatResponse>> getAllSeatsById(@PathVariable Long id){
		
		List<SeatResponse> events = eventService.getAllSeatsByEventId(id);
		return ResponseEntity.ok(events);
	}
	
	
	
}

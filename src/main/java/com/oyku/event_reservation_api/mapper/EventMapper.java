package com.oyku.event_reservation_api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.oyku.event_reservation_api.dto.event.EventCreateRequest;
import com.oyku.event_reservation_api.dto.event.EventResponse;
import com.oyku.event_reservation_api.dto.event.EventUpdateRequest;
import com.oyku.event_reservation_api.entity.Event;

@Mapper(componentModel = "spring")
public interface EventMapper {

	@Mapping(target = "seats", ignore = true)
	Event toEntity(EventCreateRequest request);

	@Mapping(target = "totalSeats", expression = "java(event.getSeats().size())")
	@Mapping(target = "availableSeats", expression = "java(countAvailableSeats(event))")
	EventResponse toResponse(Event event);

	default Integer countAvailableSeats(Event event) {
		return (int) event.getSeats().stream()
				.filter(seat -> seat.getStatus() == com.oyku.event_reservation_api.enums.SeatStatus.AVAILABLE).count();
	}

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "seats", ignore = true)
	void updateEventFromRequest(EventUpdateRequest request, @MappingTarget Event event);

	List<EventResponse> toResponseList(List<Event> events);

}

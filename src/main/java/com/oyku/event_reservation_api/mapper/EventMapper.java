package com.oyku.event_reservation_api.mapper;

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
	
	@Mapping(target = "totalSeats", ignore = true)
	@Mapping(target = "availableSeats", ignore = true)
	EventResponse toResponse(Event event);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "seats", ignore = true)
	void updateEventFromRequest(EventUpdateRequest request, @MappingTarget Event event);

}

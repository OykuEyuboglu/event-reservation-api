package com.oyku.event_reservation_api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.oyku.event_reservation_api.dto.reservation.ReservationCreateRequest;
import com.oyku.event_reservation_api.dto.reservation.ReservationResponse;
import com.oyku.event_reservation_api.entity.Reservation;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

	@Mapping(target = "expiresAt", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "event", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "seat", ignore = true)
	Reservation toEntity(ReservationCreateRequest request);
	
	@Mapping(target = "seatId", source = "seat.id")
	@Mapping(target = "eventName", source = "event.name")
	@Mapping(target = "eventId", source = "event.id")
	@Mapping(target = "seatNumber", source = "seat.seatNumber")
	ReservationResponse toResponse(Reservation reservation);
	
	List<ReservationResponse> toResponseList(List<Reservation> reservations);
}

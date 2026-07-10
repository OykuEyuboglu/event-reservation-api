package com.oyku.event_reservation_api.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.oyku.event_reservation_api.dto.seat.SeatResponse;
import com.oyku.event_reservation_api.entity.Seat;

@Mapper(componentModel = "spring")
public interface SeatMapper {

	List<SeatResponse> toResponseList(List<Seat> seats);
}

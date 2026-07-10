package com.oyku.event_reservation_api.dto.seat;

import com.oyku.event_reservation_api.enums.SeatStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatResponse {

	private Long id;
	private String seatNumber;
	private SeatStatus status;

}

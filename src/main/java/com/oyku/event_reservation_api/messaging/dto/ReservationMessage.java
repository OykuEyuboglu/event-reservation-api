package com.oyku.event_reservation_api.messaging.dto;

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
public class ReservationMessage {

	private Long reservationId;
	private Long eventId;
	private Long userId;
	private String message;
}

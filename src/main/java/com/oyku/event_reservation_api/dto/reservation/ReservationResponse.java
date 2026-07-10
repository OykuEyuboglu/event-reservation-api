package com.oyku.event_reservation_api.dto.reservation;

import java.time.LocalDateTime;

import com.oyku.event_reservation_api.enums.ReservationStatus;

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
public class ReservationResponse {

	private Long id;
	private Long eventId;
	private String eventName;
	private Long seatId;
	private String seatNumber;
	private ReservationStatus status;
	private LocalDateTime expiresAt;

}

package com.oyku.event_reservation_api.dto.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class EventResponse {

	private Long id;

	private String name;

	private String location;

	private String address;
	
	private LocalDateTime eventDate;

	private BigDecimal ticketPrice;

	private Integer totalSeats;

	private Integer availableSeats;

}

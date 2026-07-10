package com.oyku.event_reservation_api.dto.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class EventUpdateRequest {

	@NotBlank
	@Size(max = 50)
	private String name;

	@NotBlank
	@Size(max = 100)
	private String location;

	@NotBlank
	@Size(max = 200)
	private String address;

	@NotNull
	@Future
	private LocalDateTime eventDate;

	@NotNull
	@Positive
	private BigDecimal ticketPrice;
}

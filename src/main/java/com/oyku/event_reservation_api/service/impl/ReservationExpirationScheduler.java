package com.oyku.event_reservation_api.service.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.oyku.event_reservation_api.service.ReservationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationExpirationScheduler {

	private final ReservationService reservationService;
		
	@Scheduled(fixedRate = 30000)
	public void expireReservations() {
		reservationService.expireReservations();
	}
}
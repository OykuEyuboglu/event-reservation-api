package com.oyku.event_reservation_api.service;

import java.util.List;
import com.oyku.event_reservation_api.dto.reservation.ReservationCreateRequest;
import com.oyku.event_reservation_api.dto.reservation.ReservationResponse;

public interface ReservationService {

	ReservationResponse createReservation(ReservationCreateRequest request);

	List<ReservationResponse> getCurrentUserReservations();

	ReservationResponse getReservationById(Long id);

	ReservationResponse cancelReservation(Long id);
	
	void expireReservations();
	
	ReservationResponse confirmReservation(Long id);
}

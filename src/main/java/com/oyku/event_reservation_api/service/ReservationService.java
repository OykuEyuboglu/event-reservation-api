package com.oyku.event_reservation_api.service;

import java.util.List;
import com.oyku.event_reservation_api.dto.reservation.ReservationCreateRequest;
import com.oyku.event_reservation_api.dto.reservation.ReservationResponse;

public interface ReservationService {

	ReservationResponse createReservation(ReservationCreateRequest request);

	List<ReservationResponse> getReservations();

	ReservationResponse getReservationById(Long id);

	void cancelReservation(Long id);
}

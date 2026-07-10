package com.oyku.event_reservation_api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oyku.event_reservation_api.dto.reservation.ReservationCreateRequest;
import com.oyku.event_reservation_api.dto.reservation.ReservationResponse;
import com.oyku.event_reservation_api.service.ReservationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{
	
	@Override
	@Transactional
	public ReservationResponse createReservation(ReservationCreateRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReservationResponse> getReservations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public ReservationResponse getReservationById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void cancelReservation(Long id) {
		// TODO Auto-generated method stub
		
	}

}

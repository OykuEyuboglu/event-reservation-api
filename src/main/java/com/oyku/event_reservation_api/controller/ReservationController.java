package com.oyku.event_reservation_api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oyku.event_reservation_api.dto.reservation.ReservationCreateRequest;
import com.oyku.event_reservation_api.dto.reservation.ReservationResponse;
import com.oyku.event_reservation_api.service.AuthService;
import com.oyku.event_reservation_api.service.ReservationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ReservationController {

	private final ReservationService reservationService;
	private static final Logger LOGGER =
	        LoggerFactory.getLogger(AuthService.class);
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping
	public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationCreateRequest request){
		
		LOGGER.info("Create reservation method called");
		
		ReservationResponse createdReservation = reservationService.createReservation(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/my")
	public ResponseEntity<List<ReservationResponse>> getReservations(){
		
		List<ReservationResponse> reservations = reservationService.getCurrentUserReservations();
		return ResponseEntity.ok(reservations);
	}

}
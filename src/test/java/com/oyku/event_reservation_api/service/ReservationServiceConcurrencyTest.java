package com.oyku.event_reservation_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.oyku.event_reservation_api.dto.reservation.ReservationCreateRequest;
import com.oyku.event_reservation_api.repository.ReservationRepository;
import com.oyku.event_reservation_api.service.impl.ReservationServiceImpl;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
@WithMockUser(username = "email22@gmail.com", roles = "USER")
class ReservationServiceConcurrencyTest {

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired
	private static final Logger LOGGER =
	        LoggerFactory.getLogger(ReservationServiceImpl.class);

	@Test
	void shouldPreventDoubleReservation() throws InterruptedException, ExecutionException {

		ReservationCreateRequest request = new ReservationCreateRequest();
		request.setEventId(2L);
		request.setSeatId(13L);

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(
				new UsernamePasswordAuthenticationToken("email22@gmail.com", null, List.of(() -> "ROLE_USER")));
		ExecutorService executor = new DelegatingSecurityContextExecutorService(Executors.newFixedThreadPool(2),
				context);

		Future<?> first = executor.submit(() -> {

			try {
				reservationService.createReservation(request);
				LOGGER.error("SUCCESS: First reservation");
				return true;
				
			} catch (Exception e) {
				LOGGER.error("FAILED: Reservation failed for first request");
				return false;
			}

		});

		Future<?> second = executor.submit(() -> {

			try {
				reservationService.createReservation(request);
				LOGGER.error("SUCCESS: Second reservation");
				return true;

			} catch (Exception e) {
				LOGGER.error("FAILED: Reservation failed for second request");
				return false;
			}

		});

		first.get();
		second.get();
		
		boolean firstResult = (boolean) first.get();
		boolean secondResult = (boolean) second.get();

		LOGGER.info("FIRST Reservation RESULT:" + firstResult);
		LOGGER.info("SECOND Reservation RESULT:" + secondResult);
		
		executor.shutdown();

		assertEquals(1, reservationRepository.count());
	}
}

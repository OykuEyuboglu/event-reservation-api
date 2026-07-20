package com.oyku.event_reservation_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oyku.event_reservation_api.entity.Reservation;
import com.oyku.event_reservation_api.entity.User;
import com.oyku.event_reservation_api.enums.ReservationStatus;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	List<Reservation> findByUser(User user);

	List<Reservation> findByStatusAndExpiresAtBefore(ReservationStatus status, LocalDateTime now);

	long countByUserAndStatus(User user, ReservationStatus status);
}

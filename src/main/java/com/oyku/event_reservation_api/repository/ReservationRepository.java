package com.oyku.event_reservation_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oyku.event_reservation_api.entity.Reservation;
import com.oyku.event_reservation_api.entity.User;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
	List<Reservation> findByUser(User user);
}

package com.oyku.event_reservation_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oyku.event_reservation_api.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

}

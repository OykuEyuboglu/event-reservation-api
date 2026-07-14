package com.oyku.event_reservation_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.oyku.event_reservation_api.entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {


}

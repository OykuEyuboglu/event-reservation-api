package com.oyku.event_reservation_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oyku.event_reservation_api.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

	Optional<Event> findById(Long id);

	
}

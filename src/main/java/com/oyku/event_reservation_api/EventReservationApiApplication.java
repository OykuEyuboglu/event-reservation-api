package com.oyku.event_reservation_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventReservationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventReservationApiApplication.class, args);
	}

}

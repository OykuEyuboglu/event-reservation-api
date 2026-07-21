package com.oyku.event_reservation_api.messaging.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.oyku.event_reservation_api.messaging.dto.ReservationMessage;
import com.oyku.event_reservation_api.security.config.RabbitMQConfig;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReservationConsumer.class);

	@RabbitListener(queues = RabbitMQConfig.QUEUE)
	public void receiveReservation(ReservationMessage message) {
		LOGGER.info("Reservation received : {}", message);
	}
}

package com.oyku.event_reservation_api.messaging.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.oyku.event_reservation_api.messaging.dto.ReservationMessage;
import com.oyku.event_reservation_api.security.config.RabbitMQConfig;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationProducer {

	private final RabbitTemplate rabbitTemplate;

	public void sendReservationCreated(ReservationMessage message) {

		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, message);
	}
}

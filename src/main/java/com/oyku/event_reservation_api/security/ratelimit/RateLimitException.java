package com.oyku.event_reservation_api.security.ratelimit;

public class RateLimitException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public RateLimitException(String message) {
		super(message);
	}
}

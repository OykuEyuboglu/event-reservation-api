package com.oyku.event_reservation_api.exception;

public class ConflictException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public ConflictException(String message) {
		super(message);
	}
}

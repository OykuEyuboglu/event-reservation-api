package com.oyku.event_reservation_api.exception;

public class ForbiddenException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public ForbiddenException(String message) {
		super(message);
	}

}

package com.oyku.event_reservation_api.security.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.oyku.event_reservation_api.exception.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		ErrorResponse error = ErrorResponse.builder().status(HttpStatus.FORBIDDEN.value()).error("Forbidden")
				.messages("You do not have permission to access this resource.").path(request.getRequestURI()).build();

		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType("application/json");

		new ObjectMapper().writeValue(response.getOutputStream(), error);

	}

}

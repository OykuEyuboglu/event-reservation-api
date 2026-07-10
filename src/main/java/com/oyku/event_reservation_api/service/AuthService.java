package com.oyku.event_reservation_api.service;

import com.oyku.event_reservation_api.dto.auth.LoginRequest;
import com.oyku.event_reservation_api.dto.auth.LoginResponse;
import com.oyku.event_reservation_api.dto.auth.RegisterRequest;
import com.oyku.event_reservation_api.dto.auth.RegisterResponse;

public interface AuthService {

	LoginResponse login(LoginRequest request);
	
	RegisterResponse register(RegisterRequest request);
}

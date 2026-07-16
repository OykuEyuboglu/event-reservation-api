package com.oyku.event_reservation_api.service;

import com.oyku.event_reservation_api.dto.auth.AuthResponse;
import com.oyku.event_reservation_api.dto.auth.LoginRequest;
import com.oyku.event_reservation_api.dto.auth.RegisterRequest;
import com.oyku.event_reservation_api.dto.auth.RegisterResponse;

public interface AuthService {

	RegisterResponse register(RegisterRequest request);

	AuthResponse login(LoginRequest request);
}

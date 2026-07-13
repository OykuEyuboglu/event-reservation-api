package com.oyku.event_reservation_api.service;

import com.oyku.event_reservation_api.dto.auth.AuthResponse;
import com.oyku.event_reservation_api.dto.auth.LoginRequest;
import com.oyku.event_reservation_api.dto.auth.RegisterRequest;
import com.oyku.event_reservation_api.dto.auth.RegisterResponse;
import com.oyku.event_reservation_api.dto.user.UserResponse;

public interface AuthService {

	UserResponse getCurrentUser();

	RegisterResponse register(RegisterRequest request);

	AuthResponse login(LoginRequest request);
}

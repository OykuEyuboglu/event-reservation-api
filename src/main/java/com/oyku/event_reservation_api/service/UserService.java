package com.oyku.event_reservation_api.service;

import com.oyku.event_reservation_api.dto.auth.LoginRequest;
import com.oyku.event_reservation_api.dto.auth.LoginResponse;
import com.oyku.event_reservation_api.dto.auth.RegisterRequest;
import com.oyku.event_reservation_api.dto.auth.RegisterResponse;
import com.oyku.event_reservation_api.dto.user.UserResponse;

public interface UserService {

	RegisterResponse register(RegisterRequest request);

	LoginResponse login(LoginRequest request);

	UserResponse getCurrentUser();

}

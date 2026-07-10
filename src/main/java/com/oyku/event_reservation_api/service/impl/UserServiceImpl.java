package com.oyku.event_reservation_api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oyku.event_reservation_api.dto.auth.LoginRequest;
import com.oyku.event_reservation_api.dto.auth.LoginResponse;
import com.oyku.event_reservation_api.dto.auth.RegisterRequest;
import com.oyku.event_reservation_api.dto.auth.RegisterResponse;
import com.oyku.event_reservation_api.dto.user.UserResponse;
import com.oyku.event_reservation_api.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	@Override
	@Transactional
	public RegisterResponse register(RegisterRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public LoginResponse login(LoginRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public UserResponse getCurrentUser() {
		// TODO Auto-generated method stub
		return null;
	}

}

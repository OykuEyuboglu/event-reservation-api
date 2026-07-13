package com.oyku.event_reservation_api.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oyku.event_reservation_api.dto.auth.AuthResponse;
import com.oyku.event_reservation_api.dto.auth.LoginRequest;
import com.oyku.event_reservation_api.dto.auth.RegisterRequest;
import com.oyku.event_reservation_api.dto.auth.RegisterResponse;
import com.oyku.event_reservation_api.entity.User;
import com.oyku.event_reservation_api.enums.Role;
import com.oyku.event_reservation_api.exception.ConflictException;
import com.oyku.event_reservation_api.mapper.EventMapper;
import com.oyku.event_reservation_api.mapper.SeatMapperImpl;
import com.oyku.event_reservation_api.mapper.UserMapper;
import com.oyku.event_reservation_api.repository.EventRepository;
import com.oyku.event_reservation_api.repository.UserRepository;
import com.oyku.event_reservation_api.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

	@Override
	public RegisterResponse register(RegisterRequest request) {

	    if (userRepository.existsByEmail(request.getEmail())) {
	        throw new ConflictException("Email already exists.");
	    }

	    User user = userMapper.toEntity(request);

	    user.setPasswordHash(
	            passwordEncoder.encode(request.getPassword())
	    );

	    user.getRole().add(Role.USER);

	    User savedUser = userRepository.save(user);

	    return userMapper.toRegisterResponse(savedUser);

	}

	 @Override
	    public AuthResponse login(LoginRequest request) {
	        throw new UnsupportedOperationException("Login not implemented yet.");
	    }

}

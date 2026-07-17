package com.oyku.event_reservation_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.oyku.event_reservation_api.dto.auth.AuthResponse;
import com.oyku.event_reservation_api.dto.auth.LoginRequest;
import com.oyku.event_reservation_api.dto.auth.RegisterRequest;
import com.oyku.event_reservation_api.dto.auth.RegisterResponse;
import com.oyku.event_reservation_api.entity.User;
import com.oyku.event_reservation_api.enums.Role;
import com.oyku.event_reservation_api.exception.ConflictException;
import com.oyku.event_reservation_api.mapper.UserMapper;
import com.oyku.event_reservation_api.repository.UserRepository;
import com.oyku.event_reservation_api.security.jwt.JwtService;
import com.oyku.event_reservation_api.service.impl.AuthServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserMapper userMapper;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtService jwtService;

	@Mock
	private UserDetails userDetails;

	@InjectMocks
	private AuthServiceImpl authServiceImpl;

	@Test
	void shouldRegisterUserSuccessfully() {

		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@gmail.com");
		request.setPassword("123456");

		User user = new User();
		user.setRole(new HashSet<>());

		User savedUser = new User();
		savedUser.setRole(new HashSet<>(Set.of(Role.USER)));

		RegisterResponse response = new RegisterResponse();

		when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
		when(userMapper.toEntity(request)).thenReturn(user);
		when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
		when(userRepository.save(user)).thenReturn(savedUser);
		when(userMapper.toRegisterResponse(savedUser)).thenReturn(response);

		RegisterResponse result = authServiceImpl.register(request);

		assertNotNull(result);
		assertEquals("encodedPassword", user.getPasswordHash());
		assertTrue(user.getRole().contains(Role.USER));

		verify(userRepository).existsByEmail(request.getEmail());
		verify(passwordEncoder).encode(request.getPassword());
		verify(userRepository).save(user);
		verify(userMapper).toRegisterResponse(savedUser);
	}

	@Test
	void shouldThrowConflictExceptionWhenEmailAlreadyExists() {

		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@gmail.com");
		request.setPassword("123456");

		when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

		ConflictException exception = assertThrows(ConflictException.class, () -> authServiceImpl.register(request));

		assertEquals("Email already exists.", exception.getMessage());

		verify(userRepository, never()).save(any(User.class));

		verify(userMapper, never()).toEntity(any());
		verify(passwordEncoder, never()).encode(anyString());
	}

	@Test
	void shouldLoginSuccessfully() {

		LoginRequest request = new LoginRequest();
		request.setEmail("test@gmail.com");
		request.setPassword("123456");

		Authentication authentication = mock(Authentication.class);

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);

		when(authentication.getPrincipal()).thenReturn(userDetails);

		when(jwtService.generateToken(userDetails)).thenReturn("jwt-token");

		AuthResponse response = authServiceImpl.login(request);

		assertNotNull(response);
		assertEquals("jwt-token", response.getToken());

		verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(jwtService).generateToken(userDetails);
		verify(userMapper, never()).toUserResponse(null);
	}

	@Test
	void shouldThrowExceptionWhenLoginCredentialsAreInvalid() {

		LoginRequest request = new LoginRequest();
		request.setEmail("test@gmail.com");
		request.setPassword("123456");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(new BadCredentialsException("Invalid credentials"));

		BadCredentialsException exception = assertThrows(BadCredentialsException.class,
				() -> authServiceImpl.login(request));

		assertEquals("Invalid credentials", exception.getMessage());

		verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

		verify(jwtService, never()).generateToken(any(UserDetails.class));

	}

}

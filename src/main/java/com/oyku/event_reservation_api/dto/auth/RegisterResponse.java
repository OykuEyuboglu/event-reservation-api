package com.oyku.event_reservation_api.dto.auth;

import java.time.LocalDateTime;
import java.util.Set;

import com.oyku.event_reservation_api.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
	
	private Long id;
	private String name;
	private String email;
	private Set<Role> role;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}

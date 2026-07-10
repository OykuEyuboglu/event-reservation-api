package com.oyku.event_reservation_api.dto.auth;

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
public class LoginResponse {

	private String token;
	private String type;
	private String name;
	private String email;
	private Set<Role> role;

}

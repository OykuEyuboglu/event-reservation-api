package com.oyku.event_reservation_api.dto.user;

import java.util.Set;
import com.oyku.event_reservation_api.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

	private Long id;
	private String name;
	private String email;
	private Set<Role> role;

}

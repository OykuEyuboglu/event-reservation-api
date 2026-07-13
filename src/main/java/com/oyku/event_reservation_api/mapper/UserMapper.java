package com.oyku.event_reservation_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.oyku.event_reservation_api.dto.auth.RegisterRequest;
import com.oyku.event_reservation_api.dto.auth.RegisterResponse;
import com.oyku.event_reservation_api.dto.user.UserResponse;
import com.oyku.event_reservation_api.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	@Mapping(target = "role", ignore = true)
	@Mapping(target = "passwordHash", ignore = true)
	User toEntity(RegisterRequest request);
	
	RegisterResponse toRegisterResponse(User user);
	
	UserResponse toUserResponse(User user);
}

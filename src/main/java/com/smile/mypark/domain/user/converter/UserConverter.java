package com.smile.mypark.domain.user.converter;

import com.smile.mypark.domain.user.dto.response.UserResponseDTO;
import com.smile.mypark.domain.user.entity.User;

public class UserConverter {

	public static UserResponseDTO toUserResponseDTO(User user) {
		return UserResponseDTO.builder()
			.id(user.getId())
			.email(user.getEmail())
			.name(user.getName())
			.build();
	}
}
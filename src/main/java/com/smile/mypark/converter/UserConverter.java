package com.smile.mypark.converter;

import com.smile.mypark.dto.response.UserResponseDTO;
import com.smile.mypark.entity.User;

public class UserConverter {

	public static UserResponseDTO toUserResponseDTO(User user) {
		return UserResponseDTO.builder()
			.idx(user.getIdx())
			.nickname(user.getNickname())
			.build();
	}
}
package com.smile.mypark.domain.user.service;

import com.smile.mypark.domain.user.dto.request.CreateUserRequestDTO;
import com.smile.mypark.domain.user.dto.response.UserResponseDTO;
import com.smile.mypark.domain.user.entity.User;

public interface UserService {
	User createUser(CreateUserRequestDTO request);

	UserResponseDTO getUser(Long idx);
}
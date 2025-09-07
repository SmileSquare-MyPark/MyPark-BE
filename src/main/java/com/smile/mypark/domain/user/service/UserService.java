package com.smile.mypark.domain.user.service;

import com.smile.mypark.domain.user.dto.response.UserResponseDTO;

public interface UserService {
	UserResponseDTO getUser(Long userId);
}
package com.smile.mypark.domain.user.service;

import com.smile.mypark.domain.user.dto.request.CreateUserRequestDTO;
import com.smile.mypark.domain.user.dto.request.LoginRequestDTO;
import com.smile.mypark.domain.user.dto.response.UserResponseDTO;
import com.smile.mypark.global.auth.dto.TokenDTO;

import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
	void createUser(CreateUserRequestDTO request);

	TokenDTO login(LoginRequestDTO request, HttpServletResponse response);

	UserResponseDTO getUser(Long idx);
}
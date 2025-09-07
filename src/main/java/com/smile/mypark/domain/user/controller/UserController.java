package com.smile.mypark.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smile.mypark.domain.user.dto.response.UserResponseDTO;
import com.smile.mypark.domain.user.service.UserService;
import com.smile.mypark.global.annotation.AuthUser;
import com.smile.mypark.global.apipayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "[ 유저 ]")
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	@Operation(summary = "유저 조회", description = "유저 정보를 조회하는 API")
	@GetMapping
	public ApiResponse<UserResponseDTO> getUser(@AuthUser Long userId) {
		return ApiResponse.onSuccess(userService.getUser(userId));
	}
}
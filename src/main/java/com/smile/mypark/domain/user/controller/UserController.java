package com.smile.mypark.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smile.mypark.domain.user.dto.request.CreateUserRequestDTO;
import com.smile.mypark.domain.user.dto.request.LoginRequestDTO;
import com.smile.mypark.domain.user.dto.response.UserResponseDTO;
import com.smile.mypark.domain.user.service.UserService;
import com.smile.mypark.global.annotation.AuthUser;
import com.smile.mypark.global.apipayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "[ 유저 ]")
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	@Operation(summary = "회원가입", description = "회원가입 API")
	@PostMapping("/register")
	public ApiResponse<?> signUp(@RequestBody @Valid CreateUserRequestDTO request) {
		userService.createUser(request);
		return ApiResponse.onSuccess("회원가입 성공");
	}

	@Operation(summary = "로그인", description = "사용자 로그인 API")
	@PostMapping("/login")
	public ApiResponse<?> login(@RequestBody @Valid LoginRequestDTO request, HttpServletResponse response) {
		userService.login(request, response);
		return ApiResponse.onSuccess("로그인 성공");
	}

	@Operation(summary = "유저 조회", description = "유저 정보를 조회하는 API")
	@GetMapping
	public ApiResponse<UserResponseDTO> getUser(@AuthUser Long idx) {
		return ApiResponse.onSuccess(userService.getUser(idx));
	}
}
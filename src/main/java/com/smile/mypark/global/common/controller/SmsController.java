package com.smile.mypark.global.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smile.mypark.global.apipayload.ApiResponse;
import com.smile.mypark.global.common.dto.SmsRequestDTO;
import com.smile.mypark.global.common.dto.SmsVerifyRequestDTO;
import com.smile.mypark.global.common.service.SmsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "[ SMS 인증 ]")
@RequestMapping("/api/v1/sms")
public class SmsController {

	private final SmsService smsService;

    @Operation(summary = "SMS 인증번호 발송", description = "SMS 인증번호 발송 API")
	@PostMapping("/send")
	public ApiResponse<?> sendSms(@Valid @RequestBody SmsRequestDTO request) {
		smsService.sendCertificationCode(request);
		return ApiResponse.onSuccess("인증번호 발송 성공");
	}

    @Operation(summary = "SMS 인증번호 검증", description = "SMS 인증번호 검증 API")
	@PostMapping("/verify")
	public ApiResponse<?> verifySms(@Valid @RequestBody SmsVerifyRequestDTO request) {
		smsService.verifyCertificationCode(request);
		return ApiResponse.onSuccess("전화번호 인증 성공");
	}
}
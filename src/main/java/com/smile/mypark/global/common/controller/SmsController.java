package com.smile.mypark.global.common.controller;

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
@RequestMapping("/api/v1/sms")
public class SmsController {

	private final SmsService smsService;

	@PostMapping("/send")
	public ApiResponse<?> sendSms(@Valid @RequestBody SmsRequestDTO request) {
		smsService.sendCertificationCode(request);
		return ApiResponse.onSuccess("인증번호 발송 성공");
	}

	@PostMapping("/verify")
	public ApiResponse<?> verifySms(@Valid @RequestBody SmsVerifyRequestDTO request) {
		smsService.verifyCertificationCode(request);
		return ApiResponse.onSuccess("전화번호 인증 성공");
	}
}
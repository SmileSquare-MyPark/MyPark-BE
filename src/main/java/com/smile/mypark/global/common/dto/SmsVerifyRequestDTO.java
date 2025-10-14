package com.smile.mypark.global.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "SMS 인증번호 검증 요청 DTO")
public class SmsVerifyRequestDTO {

	@NotBlank(message = "전화번호는 필수입니다.")
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이어야 합니다.")
	@Schema(description = "휴대폰 번호", example = "010-1234-5678")
	private String phoneNumber;

	@NotBlank(message = "인증번호는 필수입니다.")
	@Schema(description = "SMS 인증번호", example = "123456")
	private String certificationCode;
}
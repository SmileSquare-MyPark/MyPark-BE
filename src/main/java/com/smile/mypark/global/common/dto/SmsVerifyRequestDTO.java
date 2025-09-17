package com.smile.mypark.global.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SmsVerifyRequestDTO {

	@NotBlank(message = "전화번호는 필수입니다.")
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이어야 합니다.")
	private String phoneNumber;

	@NotBlank(message = "인증번호는 필수입니다.")
	private String certificationCode;
}
package com.smile.mypark.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "로그인 요청 DTO")
public class LoginRequestDTO {
	@JsonProperty("uid")
	@NotBlank(message = "아이디는 필수입니다.")
	@Schema(description = "사용자 아이디", example = "mypark123", required = true)
	private String uId;

	@JsonProperty("password")
	@NotBlank(message = "비밀번호는 필수입니다.")
	@Schema(description = "비밀번호, 소셜 계정일 경우 소셜연동 ID", example = "password123!", required = true)
	private String password;

	@JsonProperty("kind")
	@NotBlank(message = "로그인 타입 필수입니다.")
	@Schema(description = "로그인 타입", example = "normal", allowableValues = {"normal", "kakao"})
	private String kind;
}

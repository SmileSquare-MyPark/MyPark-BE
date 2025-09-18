package com.smile.mypark.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDTO {
	@JsonProperty("uid")
	@NotBlank(message = "아이디는 필수입니다.")
	private String uId;

	@NotBlank(message = "비밀번호는 필수입니다.")
	private String password;
}

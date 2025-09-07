package com.smile.mypark.global.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDTO {
	private String accessToken;
	private String refreshToken;

	public static TokenDTO of(String accessToken, String refreshToken) {
		return TokenDTO.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
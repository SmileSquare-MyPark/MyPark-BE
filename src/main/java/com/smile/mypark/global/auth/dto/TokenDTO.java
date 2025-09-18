package com.smile.mypark.global.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDTO {
	private String accessToken;
	private String refreshToken;
	private long accessTokenExpiration;
	private long refreshTokenExpiration;

	public static TokenDTO of(String accessToken, String refreshToken, long accessTokenExpiration,
		long refreshTokenExpiration) {
		return TokenDTO.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.accessTokenExpiration(accessTokenExpiration)
			.refreshTokenExpiration(refreshTokenExpiration)
			.build();
	}
}
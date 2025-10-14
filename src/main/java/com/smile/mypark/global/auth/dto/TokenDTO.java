package com.smile.mypark.global.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "JWT 토큰 응답 DTO")
public class TokenDTO {
	@Schema(description = "액세스 토큰")
	private String accessToken;
	
	@Schema(description = "리프레시 토큰")
	private String refreshToken;
	
	@Schema(description = "액세스 토큰 만료 시간(밀리초)", example = "1640995200000")
	private long accessTokenExpiration;
	
	@Schema(description = "리프레시 토큰 만료 시간(밀리초)", example = "1643673600000")
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
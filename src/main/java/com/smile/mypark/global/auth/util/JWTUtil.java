package com.smile.mypark.global.auth.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.smile.mypark.global.auth.dto.TokenDTO;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JWTUtil {

	private final SecretKey secretKey;
	private final long accessExpiration;
	private final long refreshExpiration;
	private final long tempExpiration;

	public JWTUtil(
		@Value("${spring.security.jwt.secret}") String secret,
		@Value("${spring.security.jwt.access-token-expiration}") long accessExpiration,
		@Value("${spring.security.jwt.refresh-token-expiration}") long refreshExpiration,
		@Value("${spring.security.jwt.temp-token-expiration}") long tempExpiration
	) {
		this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm());
		this.accessExpiration = accessExpiration;
		this.refreshExpiration = refreshExpiration;
		this.tempExpiration = tempExpiration;
	}

	public String getProviderId(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("providerId", String.class);
	}

	public Date getExpiration(String token) {
		return Jwts.parser()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getExpiration();
	}

	public Boolean isExpired(String token) {
		if (token == null || token.trim().isEmpty()) {
			return true;
		}

		try {
			return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getExpiration()
				.before(new Date());
		} catch (ExpiredJwtException e) {
			return true;
		}
	}

	public String createJwt(String providerId, String role, String tokenType, Long expiredMs) {
		return Jwts.builder()
			.claim("providerId", providerId)
			.claim("role", role)
			.claim("tokenType", tokenType)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expiredMs * 1000))
			.signWith(secretKey)
			.compact();
	}

	public TokenDTO generateTokens(String providerId) {
		String access = createJwt(providerId, "ROLE_USER", "access", accessExpiration);
		String refresh = createJwt(providerId, "ROLE_USER", "refresh", refreshExpiration);

		return TokenDTO.of(access, refresh, accessExpiration, refreshExpiration);
	}

	public TokenDTO generateTempTokens(String providerId) {
		String access = createJwt(providerId, "ROLE_USER", "access", tempExpiration);
		String refresh = createJwt(providerId, "ROLE_USER", "refresh", tempExpiration);

		return TokenDTO.of(access, refresh, tempExpiration, tempExpiration);
	}
}
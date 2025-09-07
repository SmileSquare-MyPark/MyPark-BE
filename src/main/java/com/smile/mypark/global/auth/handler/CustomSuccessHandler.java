package com.smile.mypark.global.auth.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.smile.mypark.domain.user.entity.User;
import com.smile.mypark.domain.user.repository.UserRepository;
import com.smile.mypark.global.apipayload.code.status.ErrorStatus;
import com.smile.mypark.global.apipayload.exception.GeneralException;
import com.smile.mypark.global.auth.dto.CustomOAuth2User;
import com.smile.mypark.global.auth.dto.TokenDTO;
import com.smile.mypark.global.auth.util.CookieUtil;
import com.smile.mypark.global.auth.util.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Value("${app.redirect-url}")
	private String webRedirectUrl;

	private final UserRepository userRepository;
	private final JWTUtil jwtUtil;

	public CustomSuccessHandler(UserRepository userRepository, JWTUtil jwtUtil) {
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {

		CustomOAuth2User customUserDetails = (CustomOAuth2User)authentication.getPrincipal();
		String providerId = customUserDetails.getProviderId();

		TokenDTO tokenDTO = jwtUtil.generateTokens(providerId);

		String redirectUrl = generateWebToken(response, tokenDTO);

		response.sendRedirect(redirectUrl);
	}

	private String generateWebToken(HttpServletResponse response, TokenDTO tokenDTO) {
		long refreshTokenExpirationTime = jwtUtil.getExpiration(tokenDTO.getRefreshToken()).getTime();

		response.addCookie(
			CookieUtil.createCookie("accessToken", tokenDTO.getAccessToken(), refreshTokenExpirationTime));
		response.addCookie(
			CookieUtil.createCookie("refreshToken", tokenDTO.getRefreshToken(), refreshTokenExpirationTime));

		return webRedirectUrl;
	}
}
package com.smile.mypark.global.auth.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smile.mypark.global.apipayload.code.status.ErrorStatus;
import com.smile.mypark.global.auth.dto.CustomOAuth2User;
import com.smile.mypark.global.auth.dto.TokenDTO;
import com.smile.mypark.global.auth.util.ErrorResponseUtil;
import com.smile.mypark.global.auth.util.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JWTUtil jwtUtil;
	private final ObjectMapper objectMapper;

	public CustomSuccessHandler(JWTUtil jwtUtil, ObjectMapper objectMapper) {
		this.jwtUtil = jwtUtil;
		this.objectMapper = objectMapper;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {

		CustomOAuth2User customUserDetails = (CustomOAuth2User)authentication.getPrincipal();

		if (customUserDetails == null || customUserDetails.getProviderId() == null) {
			ErrorResponseUtil.sendErrorResponse(response, ErrorStatus._UNAUTHORIZED);
			return;
		}

		String providerId = customUserDetails.getProviderId();
		TokenDTO tokenDTO = jwtUtil.generateTokens(providerId);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);

		String json = objectMapper.writeValueAsString(tokenDTO);
		response.getWriter().write(json);
	}
}
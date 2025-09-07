package com.smile.mypark.global.auth.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.smile.mypark.global.apipayload.code.status.ErrorStatus;
import com.smile.mypark.global.auth.util.ErrorResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		ErrorResponseUtil.sendErrorResponse(response, ErrorStatus._UNAUTHORIZED);
	}
}
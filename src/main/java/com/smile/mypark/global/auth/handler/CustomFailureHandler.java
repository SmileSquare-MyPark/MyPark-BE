package com.smile.mypark.global.auth.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.smile.mypark.global.apipayload.code.status.ErrorStatus;
import com.smile.mypark.global.auth.util.ErrorResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomFailureHandler implements AuthenticationFailureHandler {

	@Value("${app.redirect-url}")
	String redirectUrl;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException {

		ErrorResponseUtil.sendErrorResponse(response, ErrorStatus._INTERNAL_SERVER_ERROR);
	}
}
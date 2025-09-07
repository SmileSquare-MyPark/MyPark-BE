package com.smile.mypark.global.auth.util;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smile.mypark.global.apipayload.code.status.ErrorStatus;

import jakarta.servlet.http.HttpServletResponse;

public class ErrorResponseUtil {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static void sendErrorResponse(HttpServletResponse response, ErrorStatus errorStatus) throws
		IOException {
		response.setStatus(errorStatus.getHttpStatus().value());
		response.setContentType("application/json;charset=UTF-8");

		Map<String, Object> errorResponse = new LinkedHashMap<>();
		errorResponse.put("isSuccess", false);
		errorResponse.put("code", errorStatus.getCode());
		errorResponse.put("message", errorStatus.getMessage());

		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}
package com.smile.mypark.global.interceptor;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.smile.mypark.global.annotation.AuthUser;
import com.smile.mypark.global.apipayload.code.status.ErrorStatus;
import com.smile.mypark.global.apipayload.exception.GeneralException;

@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(Long.class)
			&& parameter.hasParameterAnnotation(AuthUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory) {

		final Object userId = webRequest.getAttribute("USER_ID", WebRequest.SCOPE_REQUEST);

		if (userId == null) {
			throw new GeneralException(ErrorStatus._INVALID_AUTH_USER_ERROR);
		}

		return userId;
	}
}
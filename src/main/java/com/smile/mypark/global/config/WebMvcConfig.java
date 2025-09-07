package com.smile.mypark.global.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.smile.mypark.global.constants.Constants;
import com.smile.mypark.global.interceptor.AuthUserArgumentResolver;
import com.smile.mypark.global.interceptor.JWTInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

	private final AuthUserArgumentResolver authUserArgumentResolver;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(authUserArgumentResolver);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new JWTInterceptor())
			.addPathPatterns("/**")
			.excludePathPatterns(Constants.NO_NEED_FILTER_URLS);
	}
}
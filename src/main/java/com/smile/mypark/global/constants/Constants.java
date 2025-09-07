package com.smile.mypark.global.constants;

import java.util.List;

public final class Constants {

	private Constants() {
	}

	public static List<String> NO_NEED_FILTER_URLS = List.of(
		"/swagger-ui.html/**",
		"/v3/api-docs/**",
		"/swagger-ui/**",
		"/oauth2/authorization/**"
	);
}
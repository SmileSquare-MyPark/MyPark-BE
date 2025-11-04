package com.smile.mypark.global.auth.dto;

import java.util.Map;

public class NaverResponseDTO implements OAuth2Response {

	private final Map<String, Object> attributes;

	public NaverResponseDTO(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getProvider() {
		return "naver";
	}

	@Override
	public String getProviderId() {
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		return response != null ? response.get("id").toString() : null;
	}

	@Override
	public String getEmail() {
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		return response != null ? response.get("email").toString() : null;
	}

	@Override
	public String getName() {
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		if (response != null) {
			String nickname = (String) response.get("nickname");
			if (nickname != null) {
				return nickname;
			}
			return (String) response.get("name");
		}
		return null;
	}
}

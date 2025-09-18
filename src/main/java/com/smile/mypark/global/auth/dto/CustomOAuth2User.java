package com.smile.mypark.global.auth.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.smile.mypark.domain.user.dto.request.UserDTO;

public class CustomOAuth2User implements OAuth2User {

	private final UserDTO userDTO;
	private final boolean firstLogin;

	public CustomOAuth2User(UserDTO userDTO, boolean firstLogin) {
		this.userDTO = userDTO;
		this.firstLogin = firstLogin;
	}

	@Override
	public Map<String, Object> getAttributes() {

		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getName() {
		return userDTO.getProviderId();
	}

	public Long getId() {
		return userDTO.getId();
	}

	public String getProviderId() {
		return userDTO.getProviderId();
	}

	public boolean isFirstLogin() {
		return userDTO.getPhoneNumber() == null;
	}
}
package com.smile.mypark.global.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.smile.mypark.domain.user.dto.request.UserDTO;
import com.smile.mypark.domain.user.entity.User;
import com.smile.mypark.domain.user.repository.UserRepository;
import com.smile.mypark.global.auth.dto.CustomOAuth2User;
import com.smile.mypark.global.auth.dto.KakaoResponseDTO;
import com.smile.mypark.global.auth.dto.OAuth2Response;
import com.smile.mypark.global.auth.util.JWTUtil;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	public CustomOAuth2UserService(UserRepository userRepository, JWTUtil jwtUtil) {
		this.userRepository = userRepository;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = null;

		if (registrationId.equals("kakao")) {

			oAuth2Response = new KakaoResponseDTO(oAuth2User.getAttributes());
		} else {

			return null;
		}

		String providerId = oAuth2Response.getProviderId();

		Optional<User> existData = userRepository.findByProviderId(providerId);

		if (existData.isEmpty()) {
			User userEntity = User.builder()
				.name(oAuth2Response.getName())
				.email(oAuth2Response.getEmail())
				.providerId(providerId)
				.build();

			userRepository.save(userEntity);

			UserDTO userDTO = UserDTO.builder()
				.id(userEntity.getId())
				.name(oAuth2Response.getName())
				.email(oAuth2Response.getEmail())
				.providerId(providerId)
				.build();

			return new CustomOAuth2User(userDTO);
		} else {

			User user = existData.get();
			user.updateEmail(oAuth2Response.getEmail());
			user.updateName(oAuth2Response.getName());

			userRepository.save(user);

			UserDTO userDTO = UserDTO.builder()
				.id(user.getId())
				.name(oAuth2Response.getName())
				.email(oAuth2Response.getEmail())
				.providerId(providerId)
				.build();

			return new CustomOAuth2User(userDTO);
		}
	}
}
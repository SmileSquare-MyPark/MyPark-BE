package com.smile.mypark.global.auth.service;

import com.smile.mypark.dto.request.UserDTO;
import com.smile.mypark.entity.User;
import com.smile.mypark.global.apipayload.code.status.ErrorStatus;
import com.smile.mypark.global.apipayload.exception.GeneralException;
import com.smile.mypark.global.auth.dto.*;
import com.smile.mypark.global.auth.util.JWTUtil;
import com.smile.mypark.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

	public CustomOAuth2UserService(UserRepository userRepository, JWTUtil jwtUtil) {
		this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = null;

		if (registrationId.equals("kakao")) {

			oAuth2Response = new KakaoResponseDTO(oAuth2User.getAttributes());
		} else if (registrationId.equals("naver")) {

			oAuth2Response = new NaverResponseDTO(oAuth2User.getAttributes());
		} else {

			return null;
		}

		String providerId = oAuth2Response.getProviderId();
		Optional<User> existData = userRepository.findByuIdx(Long.valueOf(providerId));

		User userEntity;
		if (existData.isEmpty()) {

			return new CustomOAuth2User(
				UserDTO.builder()
					.providerId(providerId)
					.nickname(oAuth2Response.getName())
					.build()
			);
		} else {

			userEntity = existData.get();
		}

		UserDTO userDTO = UserDTO.builder()
			.id(userEntity.getIdx())
			.nickname(userEntity.getNickname())
			.uId(userEntity.getUId())
			.providerId(providerId)
			.build();

		return new CustomOAuth2User(userDTO);
	}

    @Transactional
    public TokenDTO reissue(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        TokenDTO tokenDTO = jwtUtil.generateTokens(String.valueOf(user.getUIdx()));

        return TokenDTO.builder()
                .accessToken(tokenDTO.getAccessToken())
                .refreshToken(tokenDTO.getRefreshToken())
                .build();
    }
}
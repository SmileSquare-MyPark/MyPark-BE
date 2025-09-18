package com.smile.mypark.domain.user.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smile.mypark.domain.user.converter.UserConverter;
import com.smile.mypark.domain.user.dto.request.CreateUserRequestDTO;
import com.smile.mypark.domain.user.dto.request.LoginRequestDTO;
import com.smile.mypark.domain.user.dto.response.UserResponseDTO;
import com.smile.mypark.domain.user.entity.User;
import com.smile.mypark.domain.user.repository.UserRepository;
import com.smile.mypark.global.apipayload.code.status.ErrorStatus;
import com.smile.mypark.global.apipayload.exception.GeneralException;
import com.smile.mypark.global.auth.dto.TokenDTO;
import com.smile.mypark.global.auth.util.JWTUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JWTUtil jwtUtil;

	@Override
	@Transactional
	public void createUser(CreateUserRequestDTO request) {
		if (userRepository.existsByuId(request.getUId())) {
			throw new GeneralException(ErrorStatus._USER_ID_DUPLICATE);
		}

		String encodedPassword = passwordEncoder.encode(request.getPassword());

		User user = User.builder()
			.uId(request.getUId())
			.password(encodedPassword)
			.nickname(request.getNickname())
			.kind(request.getKind())
			.height(request.getHeight())
			.weight(request.getWeight())
			.age(request.getAge())
			.isAgreePos(request.getIsAgreePos())
			.isAgreeAlert(request.getIsAgreeAlert())
			.uIdx(request.getUIdx())
			.regDate(LocalDateTime.now())
			.lastDate(LocalDateTime.now())
			.build();

		userRepository.save(user);
	}

	@Override
	@Transactional
	public TokenDTO login(LoginRequestDTO request, HttpServletResponse response) {
		User user = userRepository.findByuId(request.getUId())
			.orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new GeneralException(ErrorStatus._INVALID_PASSWORD);
		}

		TokenDTO tokenDTO = jwtUtil.generateTokens(String.valueOf(user.getUIdx()));

		return TokenDTO.builder()
			.accessToken(tokenDTO.getAccessToken())
			.refreshToken(tokenDTO.getRefreshToken())
			.build();
	}

	@Override
	@Transactional(readOnly = true)
	public UserResponseDTO getUser(Long idx) {
		User user = userRepository.findByIdx(idx)
			.orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

		return UserConverter.toUserResponseDTO(user);
	}
}
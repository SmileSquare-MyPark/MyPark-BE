package com.smile.mypark.domain.user.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smile.mypark.domain.user.converter.UserConverter;
import com.smile.mypark.domain.user.dto.request.CreateUserRequestDTO;
import com.smile.mypark.domain.user.dto.response.UserResponseDTO;
import com.smile.mypark.domain.user.entity.User;
import com.smile.mypark.domain.user.repository.UserRepository;
import com.smile.mypark.global.apipayload.code.status.ErrorStatus;
import com.smile.mypark.global.apipayload.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public User createUser(CreateUserRequestDTO request) {
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

		return userRepository.save(user);
	}

	@Override
	@Transactional(readOnly = true)
	public UserResponseDTO getUser(Long idx) {
		User user = userRepository.findByuIdx(idx)
			.orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

		return UserConverter.toUserResponseDTO(user);
	}
}
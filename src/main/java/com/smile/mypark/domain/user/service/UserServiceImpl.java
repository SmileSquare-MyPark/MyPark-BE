package com.smile.mypark.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smile.mypark.domain.user.converter.UserConverter;
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

	@Override
	public UserResponseDTO getUser(Long userId) {
		User user = userRepository.findByIdAndNotDeleted(userId)
			.orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

		return UserConverter.toUserResponseDTO(user);
	}
}
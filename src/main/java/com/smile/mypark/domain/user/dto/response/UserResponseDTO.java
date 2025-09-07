package com.smile.mypark.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDTO {
	private Long id;
	private String email;
	private String name;
}
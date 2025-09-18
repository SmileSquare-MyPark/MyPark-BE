package com.smile.mypark.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDTO {
	private Long id;
	private String uId;
	private String nickname;
	private String providerId;
	private String phoneNumber;
}
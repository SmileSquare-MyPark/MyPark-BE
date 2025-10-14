package com.smile.mypark.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "사용자 정보 응답 DTO")
public class UserResponseDTO {
	@Schema(description = "사용자 인덱스", example = "1")
	private Long idx;
	
	@Schema(description = "사용자 닉네임", example = "MyPark")
	private String nickname;
}
package com.smile.mypark.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "회원가입 요청 DTO")
public class CreateUserRequestDTO {

	@JsonProperty("uid")
	@Schema(description = "사용자 아이디", example = "mypark123")
	private String uId;

	@JsonProperty("password")
	@Schema(description = "비밀번호", example = "password123!")
	private String password;

	@JsonProperty("nickname")
	@Schema(description = "닉네임", example = "MyPark")
	private String nickname;

	@JsonProperty("kind")
	@Schema(description = "회원가입 타입", example = "normal", allowableValues = {"normal", "kakao"})
	private String kind;

	@JsonProperty("height")
	@Schema(description = "키(cm)", example = "175", minimum = "100", maximum = "250")
	private Integer height;

	@JsonProperty("weight")
	@Schema(description = "몸무게(kg)", example = "70", minimum = "30", maximum = "200")
	private Integer weight;

	@JsonProperty("age")
	@Schema(description = "나이", example = "25", minimum = "1", maximum = "100")
	private Integer age;

	@JsonProperty("gender")
	@Schema(description = "성별 (1: 남성, 2: 여성)", example = "1", allowableValues = {"0", "1", "2"})
	private Integer gender;

	@JsonProperty("isAgreePos")
	@Schema(description = "위치 정보 이용 동의", example = "true")
	private Boolean isAgreePos;

	@JsonProperty("isAgreeAlert")
	@Schema(description = "알림 수신 동의", example = "true")
	private Boolean isAgreeAlert;

	@JsonProperty("uidx")
	@Schema(description = "소셜연동 ID", example = "123456789")
	private Long uIdx;
}
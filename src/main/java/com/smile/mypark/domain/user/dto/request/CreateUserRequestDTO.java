package com.smile.mypark.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserRequestDTO {

	@JsonProperty("uid")
	private String uId;

	@JsonProperty("password")
	private String password;

	@JsonProperty("nickname")
	private String nickname;

	@JsonProperty("kind")
	private String kind;

	@JsonProperty("height")
	private Integer height;

	@JsonProperty("weight")
	private Integer weight;

	@JsonProperty("age")
	private Integer age;

	@JsonProperty("gender")
	private Integer gender;

	@JsonProperty("isAgreePos")
	private Boolean isAgreePos;

	@JsonProperty("isAgreeAlert")
	private Boolean isAgreeAlert;

	@JsonProperty("uidx")
	private Long uIdx;
}
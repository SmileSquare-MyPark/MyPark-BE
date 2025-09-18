package com.smile.mypark.domain.user.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_User", schema = "dbo")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idx", nullable = false)
	private Long idx;

	@Column(name = "u_id")
	private String uId;

	@Column(name = "u_pwd")
	private String password;

	@Column(name = "u_nick")
	private String nickname;

	@Column(name = "u_kind")
	private String kind;

	@Column(name = "u_height")
	private Integer height;

	@Column(name = "u_weight")
	private Integer weight;

	@Column(name = "u_age")
	private Integer age;

	@Column(name = "u_gen")
	private Integer gender;

	@Column(name = "isAgreePos")
	private Boolean isAgreePos;

	@Column(name = "isAgreeAlert")
	private Boolean isAgreeAlert;

	@Column(name = "u_idx")
	private Long uIdx;

	@Column(name = "u_regdate")
	private LocalDateTime regDate;

	@Column(name = "u_lastdate")
	private LocalDateTime lastDate;

	@Builder
	public User(String uId,
				String password,
				String nickname,
				String kind,
				Integer height,
				Integer weight,
				Integer age,
				Integer gender,
				Boolean isAgreePos,
				Boolean isAgreeAlert,
				Long uIdx,
				LocalDateTime regDate,
				LocalDateTime lastDate) {
		this.uId = uId;
		this.password = password;
		this.nickname = nickname;
		this.kind = kind;
		this.height = height;
		this.weight = weight;
		this.age = age;
		this.gender = gender;
		this.isAgreePos = isAgreePos;
		this.isAgreeAlert = isAgreeAlert;
		this.uIdx = uIdx;
		this.regDate = regDate;
		this.lastDate = lastDate;
	}
}
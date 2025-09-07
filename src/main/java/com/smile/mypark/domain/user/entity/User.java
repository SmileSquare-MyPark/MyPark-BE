package com.smile.mypark.domain.user.entity;

import com.smile.mypark.global.entity.BaseEntity;

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
@Table(name = "users")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "email", unique = true, nullable = true)
	private String email;

	@Column(name = "provider_id", nullable = false)
	private String providerId;

	@Builder
	public User(String name, String providerId, String email) {
		this.name = name;
		this.email = email;
		this.providerId = providerId;
	}

	public void updateEmail(String email) {
		this.email = email;
	}

	public void updateName(String name) {
		this.name = name;
	}
}
package com.smile.mypark.global.apipayload.code.status;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessStatus {
	_OK(HttpStatus.OK, "200", "OK"),
	_CREATED(HttpStatus.CREATED, "201", "Created");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}

package com.smile.mypark.global.apipayload.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ErrorReasonDTO {
	String message;
	String code;
	Boolean isSuccess;
	HttpStatus httpStatus;
}

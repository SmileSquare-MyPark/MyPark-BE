package com.smile.mypark.global.apipayload.exception;

import com.smile.mypark.global.apipayload.code.BaseErrorCode;
import com.smile.mypark.global.apipayload.code.ErrorReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

	private BaseErrorCode code;

	public ErrorReasonDTO getErrorReasonHttpStatus() {
		return this.code.getReasonHttpStatus();
	}
}

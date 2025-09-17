package com.smile.mypark.global.apipayload.code.status;

import org.springframework.http.HttpStatus;

import com.smile.mypark.global.apipayload.code.BaseErrorCode;
import com.smile.mypark.global.apipayload.code.ErrorReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

	_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5000", "서버 에러. 관리자에게 문의하세요."),
	_BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON4000", "잘못된 요청입니다."),
	_INVALID_AUTH_USER_ERROR(HttpStatus.BAD_REQUEST, "COMMON4001", "사용자 인증에 실패했습니다."),

	_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4000", "사용자를 찾을 수 없습니다."),

	_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH4000", "인증되지 않은 사용자입니다. 로그인 후 다시 시도해주세요."),

	_TOKEN_NOT_EXISTS(HttpStatus.UNAUTHORIZED, "AUTH4000", "토큰이 존재하지 않습니다. 로그인 후 다시 시도해주세요."),
	_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH4001", "토큰이 만료되었습니다. 로그인 후 다시 시도해주세요."),

	_SMS_ALREADY_SEND(HttpStatus.BAD_REQUEST, "SMS4001", "이미 인증 코드가 발송되었습니다."),
	_SMS_CERTIFICATION_EXPIRED(HttpStatus.BAD_REQUEST, "SMS4002", "인증 코드가 만료되었습니다."),
	_SMS_CERTIFICATION_MISMATCH(HttpStatus.BAD_REQUEST, "SMS4003", "인증 코드가 일치하지 않습니다."),
	_SMS_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "SMS5001", "SMS 전송에 실패했습니다."),;

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	@Override
	public ErrorReasonDTO getReasonHttpStatus() {
		return ErrorReasonDTO.builder()
			.message(message)
			.code(code)
			.isSuccess(false)
			.httpStatus(httpStatus)
			.build();
	}
}

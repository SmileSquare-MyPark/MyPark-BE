package com.smile.mypark.global.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class DuplicatedEmailException extends AuthenticationException {
	public DuplicatedEmailException(String msg) {
		super(msg);
	}
}
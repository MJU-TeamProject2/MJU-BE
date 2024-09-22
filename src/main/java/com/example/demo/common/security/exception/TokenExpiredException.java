package com.example.demo.common.security.exception;

public class TokenExpiredException extends TokenException {
	public TokenExpiredException() {
		super(SecurityErrorCode.EXPIRED_TOKEN);
	}
}

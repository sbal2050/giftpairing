package com.example.giftpairing.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author sulagnabal
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class GiftPairingException extends RuntimeException{

	private static final long serialVersionUID = 4281687546983723197L;
	private final ErrorCode errorCode;

	public GiftPairingException(String message) {
		super(message);
		this.errorCode = ErrorCode.UNSUPPORTED_OPERATION;
	}

	public GiftPairingException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

}

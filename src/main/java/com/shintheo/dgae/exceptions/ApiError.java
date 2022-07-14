package com.shintheo.dgae.exceptions;

import java.time.ZonedDateTime;

public class ApiError {

	private final String message;
	private final ZonedDateTime timestamp;

	public ApiError(String message, ZonedDateTime timestamp) {
		super();
		this.message = message;
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

}

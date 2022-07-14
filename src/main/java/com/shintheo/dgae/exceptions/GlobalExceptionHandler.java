package com.shintheo.dgae.exceptions;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.swing.tree.ExpandVetoException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = { ExpandVetoException.class })
	public ApiError handleEmptyValueException(EmptyValueException e) {
		ApiError apiError = new ApiError(e.getMessage(), ZonedDateTime.now(ZoneId.of("z")));
		return apiError;
	}

	@ExceptionHandler(value = { NotFoundException.class })
	public ApiError handleNotFoundException(NotFoundException e) {
		ApiError apiError = new ApiError(e.getMessage(), ZonedDateTime.now(ZoneId.of("z")));
		return apiError;
	}

	@ExceptionHandler(value = { DuplicatedValueException.class })
	public ApiError handleDuplicatedValueException(DuplicatedValueException e) {
		ApiError apiError = new ApiError(e.getMessage(), ZonedDateTime.now(ZoneId.of("Z")));
		return apiError;
	}

}

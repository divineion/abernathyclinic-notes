package com.medilabo.abernathyclinic.notes.exceptions;

@SuppressWarnings("serial")
public class ForbiddenAccessException extends RuntimeException {
	public ForbiddenAccessException(String message) {
		super(message);
	}
}

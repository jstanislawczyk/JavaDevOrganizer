package com.javadev.organizer.exceptions;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Long userId;

	public UserNotFoundException(long userId) {
		this.userId = userId;
	}

	public UserNotFoundException() {
	}

	public Long getUserId() {
		return userId;
	}
}

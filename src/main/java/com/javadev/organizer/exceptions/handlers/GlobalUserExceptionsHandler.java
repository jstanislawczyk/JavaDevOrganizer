package com.javadev.organizer.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.javadev.organizer.exceptions.PresenceNotSavedException;
import com.javadev.organizer.exceptions.UserNotFoundException;
import com.javadev.organizer.exceptions.UsersListNotFoundException;

@RestControllerAdvice
public class GlobalUserExceptionsHandler {

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Error userNotFoundExceptionHandler(UserNotFoundException exception) {
		long userId = exception.getUserId();
		return new Error(404, "User [id=" + userId + "] not found");
	}

	@ExceptionHandler(UsersListNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Error usersListNotFoundExceptionHandler() {
		return new Error(404, "Users not found");
	}
	
	@ExceptionHandler(PresenceNotSavedException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Error presenceNotSavedExceptionHandler() {
		return new Error(404, "Presence not saved");
	}
}

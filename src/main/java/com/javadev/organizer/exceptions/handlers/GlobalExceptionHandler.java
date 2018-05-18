package com.javadev.organizer.exceptions.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.javadev.organizer.exceptions.NotDeletedException;
import com.javadev.organizer.exceptions.NotFoundException;
import com.javadev.organizer.exceptions.NotSavedException;
import com.javadev.organizer.exceptions.NotUniqueException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
    @ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
	public Error internalServerErrorHandler() {
		logger.error("EXCEPTION | Internal server error [500]");
		return new Error(418, "Congratulations. You have found an easter egg");
	}
    
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Error notFoundHandler(NotFoundException notFoundException) {
		logger.info("EXCEPTION | [email="+getCurrentUserEmail()+"]: "+notFoundException.getMessage());
		return new Error(404, notFoundException.getMessage());
	}
	
	@ExceptionHandler(NotUniqueException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Error notUniqueHandler(NotUniqueException notUniqueException) {
		logger.info("EXCEPTION | [email="+getCurrentUserEmail()+"]: "+notUniqueException.getMessage());
		return new Error(409, notUniqueException.getMessage());
	}
	
	@ExceptionHandler(NotSavedException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Error notSavedHandler(NotSavedException notSavedException) {
		logger.info("EXCEPTION | [email="+getCurrentUserEmail()+"]: "+notSavedException.getMessage());
		return new Error(409, notSavedException.getMessage());
	}
	
	@ExceptionHandler(NotDeletedException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Error notDeletedHandler(NotDeletedException notDeletedException) {
		logger.info("EXCEPTION | [email="+getCurrentUserEmail()+"]: "+notDeletedException.getMessage());
		return new Error(409, notDeletedException.getMessage());
	}
	
	private String getCurrentUserEmail() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
}

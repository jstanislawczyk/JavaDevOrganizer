package com.javadev.organizer.logging;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.javadev.organizer.config.SecurityConfig;
import com.javadev.organizer.exceptions.handlers.GlobalExceptionHandler;

@Aspect
@Component
public class AdminLoggingAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@Pointcut("execution(* com.javadev.organizer.controllers.AdminController.getAllUsers())")
	public void getAllUsers() {}
	
	@Before("getAllUsers()")
	public void logBeforeGetAllUsers() {
		logger.info("JAVADEV | Admin [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempt to get all users");
	}
	
	@AfterReturning("getAllUsers()")
	public void logAfterGetAllUsers() {
		logger.info("JAVADEV | Admin [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] received all users");
	}
}

package com.javadev.organizer.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.javadev.organizer.config.SecurityConfig;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.exceptions.handlers.GlobalExceptionHandler;

@Aspect
@Component
public class AdminLoggingAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@Pointcut("execution(* com.javadev.organizer.controllers.AdminController.getAllUsers())")
	public void getAllUsers() {}
	
	@Before("getAllUsers()")
	public void logBeforeGetAllUsers() {
		logger.info("JAVADEV | Admin [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempts to get all users");
	}
	
	@AfterReturning("getAllUsers()")
	public void logAfterGetAllUsers() {
		logger.info("JAVADEV | Admin [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] received all users");
	}
	
	@Before("execution(* com.javadev.organizer.controllers.AdminController.saveUser(..)) && args(..)")
	public void logBeforeSaveUser(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		User user = (User) args[0];
		
		logger.info("JAVADEV | Admin [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempts to save user [email="+user.getEmail()+"]");
	}
	
	@AfterReturning("execution(* com.javadev.organizer.controllers.AdminController.saveUser(..)) && args(..)")
	public void logAfterSaveUser(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		User user = (User) args[0];
		
		logger.info("JAVADEV | Admin [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] saved user [email="+user.getEmail()+"]");
	}
}

package com.javadev.organizer.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.javadev.organizer.config.SecurityConfig;
import com.javadev.organizer.dto.UserDto;

@Aspect
@Component
public class AdminLoggingAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminLoggingAspect.class);
	
	@Pointcut("execution(* com.javadev.organizer.controllers.AdminController.getAllUsers())")
	public void getAllUsers() {}
	
	@Pointcut("execution(* com.javadev.organizer.controllers.AdminController.deleteUser(Long)) && args(id)")
	public void deleteUser(Long id) {}
	
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
		UserDto user = (UserDto) args[0];
		
		logger.info("JAVADEV | Admin [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempts to save user [email="+user.getEmail()+"]");
	}
	
	@AfterReturning("execution(* com.javadev.organizer.controllers.AdminController.saveUser(..)) && args(..)")
	public void logAfterSaveUser(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		UserDto user = (UserDto) args[0];
		
		logger.info("JAVADEV | Admin [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] saved user [email="+user.getEmail()+"]");
	}
	
	@AfterReturning("execution(* com.javadev.organizer.controllers.AdminController.updateUser(..)) && args(..)")
	public void logAfterUpdateUser(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		UserDto user = (UserDto) args[0];
		
		logger.info("JAVADEV | Admin [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] updated user [id="+user.getId()+"]");
	}
	
	@Before("deleteUser(id)")
	public void logBeforeDeleteUser(Long id) {
		logger.info("JAVADEV | Admin [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempts to delete user [id="+id+"]");
	}
	
	@After("deleteUser(id)")
	public void logAfterDeleteUser(Long id) {
		logger.info("JAVADEV | Admin [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] deleted user [id="+id+"]");
	}
}

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
public class StudentLoggingAspect {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@Pointcut("execution(* com.javadev.organizer.controllers.StudentController.registerUserPresence(Long, Long, boolean)) && args(userId, courseId, present)")
	public void registerUserPresence(Long userId, Long courseId, boolean present) {}
	
	@Pointcut("execution(* com.javadev.organizer.controllers.StudentController.getCoursesStatusByUserId(Long)) && args(id)")
	public void getCoursesStatusByUserId(Long id) {}
	
	
	@Before("registerUserPresence(userId, courseId, present)")
	public void logBeforeRegisterUserPresence(Long userId, Long courseId, boolean present) {
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempt to save presence [courseId="+courseId+", userId="+userId+"]");
	}
	
	@AfterReturning("registerUserPresence(userId, courseId, present)")
	public void logAfterRegisterUserPresence(Long userId, Long courseId, boolean present) {
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] save presence [courseId="+courseId+", userId="+userId+"]");
	}
	
	@Before("getCoursesStatusByUserId(id)")
	public void logBeforeGetCoursesStatusByUserId(Long id) {
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempt to get courses status for user [userId="+id+"]");
	}
	
	@AfterReturning("getCoursesStatusByUserId(id)")
	public void logAfterGetCoursesStatusByUserId(Long id) {
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] received courses status for user [userId="+id+"]");
	}
}

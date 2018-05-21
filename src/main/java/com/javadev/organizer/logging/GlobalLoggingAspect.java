package com.javadev.organizer.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.javadev.organizer.config.SecurityConfig;
import com.javadev.organizer.exceptions.handlers.GlobalExceptionHandler;

@Aspect
@Component
public class GlobalLoggingAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@Pointcut("execution(* com.javadev.organizer.controllers.*.get*ById(Long)) && args(id)")
	public void objectById(Long id) {}
	
	@Before("objectById(id)")
	public void logBeforeGetObjectById(JoinPoint joinPoint, Long id) {
		String returnType = getReturnTypeName(joinPoint);

		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempts to get entity [class="+returnType+", id="+id+"]");
	}
	
	@AfterReturning("objectById(id)")
	public void logAfterGetObjectById(JoinPoint joinPoint, Long id) {
		String returnType = getReturnTypeName(joinPoint);
		
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] received entity [class="+returnType+", id="+id+"]");
	}
	
	private String getReturnTypeName(JoinPoint joinPoint) {
		Signature signature =  joinPoint.getSignature();
		return ((MethodSignature) signature).getReturnType().getSimpleName();
	}
}

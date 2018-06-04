package com.javadev.organizer.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.javadev.organizer.dto.UserDto;

@Aspect
@Component
public class TokenLoggingAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(TokenLoggingAspect.class);

	@Pointcut("execution(* com.javadev.organizer.controllers.TokenController.generate(..)) %% args(..)")
	public void generateToken(){}
	
	@Before("generateToken()")
	public void logBeforeGenerateToken(JoinPoint joinPoint) {	
		Object[] args = joinPoint.getArgs();
		UserDto user = (UserDto) args[0];
		
		logger.info("User [email="+user.getEmail()+"] attempts to get authentication token");
	}
	
	@AfterReturning("generateToken()")
	public void logAfterGenerateToken(JoinPoint joinPoint) {	
		Object[] args = joinPoint.getArgs();
		UserDto user = (UserDto) args[0];
		
		logger.info("User [email="+user.getEmail()+"] received authentication token");
	}
}

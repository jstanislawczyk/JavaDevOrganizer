package com.javadev.organizer.logging;

import org.aspectj.lang.annotation.After;
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
public class CourseLoggingAspect {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@Pointcut("execution(* com.javadev.organizer.controllers.CourseController.getCourseById(Long)) && args(id)")
	public void courseById(Long id) {}
	
	@Pointcut("execution(* com.javadev.organizer.controllers.CourseController.getAllCourses())")
	public void coursesList() {}

	@Pointcut("execution(* com.javadev.organizer.controllers.CourseController.deleteCourse(Long)) && args(id)")
	public void deleteCourse(Long id) {}
	
	
	@Before("courseById(id)")
	public void logBeforeGetCourseById(Long id) {
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempt to get course [id="+id+"]");
	}
	
	@AfterReturning("courseById(id)")
	public void logAfterGetCourseById(Long id) {
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] received course [id="+id+"]");
	}
	
	@Before("coursesList()")
	public void logBeforeGetAllCourses() {
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempt to get all courses");
	}
	
	@After("coursesList()")
	public void logAfterGetAllCourses() {
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] received all courses");
	}
	
	@Before("deleteCourse(id)")
	public void logBeforeDeleteCourse(Long id) {
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempt to delete course [id="+id+"]");
	}
	
	@After("deleteCourse(id)")
	public void logAfterDeleteCourse(Long id) {
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] deleted course [id="+id+"]");
	}
}

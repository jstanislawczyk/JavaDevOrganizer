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
import com.javadev.organizer.dto.CourseDto;
import com.javadev.organizer.entities.Course;
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
	
	@Pointcut("execution(* com.javadev.organizer.controllers.CourseController.saveCourse(..)) && args(..)")
	public void saveCourse() {}
	
	
	@Before("courseById(id)")
	public void logBeforeGetCourseById(Long id) {
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempts to get course [id="+id+"]");
	}
	
	@AfterReturning("courseById(id)")
	public void logAfterGetCourseById(Long id) {
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] received course [id="+id+"]");
	}
	
	@Before("coursesList()")
	public void logBeforeGetAllCourses() {
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempts to get all courses");
	}
	
	@AfterReturning("coursesList()")
	public void logAfterGetAllCourses() {
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] received all courses");
	}
	
	@Before("saveCourse()")
	public void logBeforeSaveCourse(JoinPoint joinPoint) {	
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempts to save course");
	}
	
	@AfterReturning("saveCourse()")
	public void logAfterSaveCourse(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		Course course = (Course) args[0];
		
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] saved course [id="+course.getId()+"]");
	}
	
	@AfterReturning("execution(* com.javadev.organizer.controllers.CourseController.updateCourse(..)) && args(..)")
	public void logAfterUpdateCourse(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		CourseDto course = (CourseDto) args[0];
		
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] updated course [id="+course.getId()+"]");
	}
	
	@Before("deleteCourse(id)")
	public void logBeforeDeleteCourse(Long id) {
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempts to delete course [id="+id+"]");
	}
	
	@After("deleteCourse(id)")
	public void logAfterDeleteCourse(Long id) {
		logger.info("JAVADEV | User [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] deleted course [id="+id+"]");
	}
}

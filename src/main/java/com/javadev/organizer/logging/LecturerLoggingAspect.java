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
import com.javadev.organizer.entities.Course;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.exceptions.handlers.GlobalExceptionHandler;

@Aspect
@Component
public class LecturerLoggingAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@Pointcut("execution(* com.javadev.organizer.controllers.LecturerController.getUserById(Long)) && args(id)")
	public void studentById(Long id) {}
	
	@Pointcut("execution(* com.javadev.organizer.controllers.LecturerController.getAllStudents())")
	public void studentsList() {}

	@Pointcut("execution(* com.javadev.organizer.controllers.LecturerController.deleteStudent(Long)) && args(id)")
	public void deleteStudent(Long id) {}
	
	@Pointcut("execution(* com.javadev.organizer.controllers.LecturerController.saveStudent(..)) && args(..)")
	public void saveStudent() {}
	
	
	@Before("studentById(id)")
	public void logBeforeGetStudentById(Long id) {
		logger.info("JAVADEV | Lecturer [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempts to get student [id="+id+"]");
	}
	
	@AfterReturning("studentById(id)")
	public void logAfterGetStudentById(Long id) {
		logger.info("JAVADEV | Lecturer [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] received student [id="+id+"]");
	}
	
	@Before("studentsList()")
	public void logBeforeGetAllStudents() {
		logger.info("JAVADEV | Lecturer [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempts to get all students");
	}
	
	@AfterReturning("studentsList()")
	public void logAfterGetAllStudents() {
		logger.info("JAVADEV | Lecturer [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] received all students");
	}
	
	@Before("saveStudent()")
	public void logBeforeSaveStudent(JoinPoint joinPoint) {	
		logger.info("JAVADEV | Lecturer [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempts to save student");
	}
	
	@AfterReturning("saveStudent()")
	public void logAfterSaveStudent(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		Course course = (Course) args[0];
		
		logger.info("JAVADEV | Lecturer [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] saved student [id="+course.getId()+"]");
	}
	
	@AfterReturning("execution(* com.javadev.organizer.controllers.CourseController.updateStudent(..)) && args(..)")
	public void logAfterUpdateStudent(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		User student = (User) args[0];
		
		logger.info("JAVADEV | Lecturer [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] updated student [id="+student.getId()+"]");
	}
	
	@Before("deleteStudent(id)")
	public void logBeforeDeleteStudent(Long id) {
		logger.info("JAVADEV | Lecturer [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] attempts to delete student [id="+id+"]");
	}
	
	@After("deleteStudent(id)")
	public void logAfterDeleteStudent(Long id) {
		logger.info("JAVADEV | Lecturer [email="+SecurityConfig.getCurrentLoggedInUserEmail()+"] deleted student [id="+id+"]");
	}
}
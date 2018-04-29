package com.javadev.organizer.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.javadev.organizer.exceptions.CourseNotCreatedException;
import com.javadev.organizer.exceptions.CourseNotDeletedException;
import com.javadev.organizer.exceptions.CourseNotFoundException;
import com.javadev.organizer.exceptions.CoursesListNotFoundException;

@RestControllerAdvice
public class GlobalCourseExceptionsHandler {

	@ExceptionHandler(CoursesListNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Error coursesListNotFound() {
		return new Error(404, "Courses not found");
	}

	@ExceptionHandler(CourseNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Error courseNotFound(CourseNotFoundException exception) {
		long courseId = exception.getCourseId();
		return new Error(404, "Course [id=" + courseId + "] not found");
	}

	@ExceptionHandler(CourseNotCreatedException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Error courseNotCreated() {
		return new Error(409, "Course not created. Can't create more than 8 courses");
	}
	
	@ExceptionHandler(CourseNotDeletedException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Error courseNotDeleted(CourseNotDeletedException exception) {
		long courseId = exception.getCourseId();
		return new Error(409, "Course [id="+courseId+"] not deleted. Can't delete course after his start date");
	}
}

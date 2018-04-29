package com.javadev.organizer.exceptions;

public class CourseNotDeletedException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private long courseId;

	public CourseNotDeletedException(long courseId) {
		super();
		this.courseId = courseId;
	}

	public long getCourseId() {
		return courseId;
	}
}

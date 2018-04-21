package com.javadev.organizer.exceptions;

public class CourseNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private long courseId;

	public CourseNotFoundException(long courseId) {
		super();
		this.courseId = courseId;
	}

	public long getCourseId() {
		return courseId;
	}
}

package com.javadev.organizer.repositories;

import org.springframework.data.repository.CrudRepository;

import com.javadev.organizer.entities.Course;

public interface CourseRepository extends CrudRepository<Course, Long> {
	
}

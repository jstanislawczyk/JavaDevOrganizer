package com.javadev.organizer.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javadev.organizer.entities.Course;
import com.javadev.organizer.exceptions.NotDeletedException;
import com.javadev.organizer.exceptions.NotFoundException;
import com.javadev.organizer.exceptions.NotSavedException;
import com.javadev.organizer.repositories.CourseRepository;

@Service
public class CourseService {
	
	@Autowired
	private CourseRepository courseRepository;

	public Course getCourseById(Long id) throws NotFoundException {
		return courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Course [id="+id+"] not found"));
	}
	
	public List<Course> getAllCourses() throws NotFoundException {
		List<Course> courses = new ArrayList<>();
		courseRepository.findAll().forEach(courses::add);
		Collections.sort(courses, (first, second) -> first.getDate().compareTo(second.getDate()));
		
		return courses;
	}
	
	public Course saveCourse(Course course) throws NotSavedException {
		if (courseRepository.count() < 8) {
			courseRepository.save(course);
			
			return course;
		} else {
			throw new NotSavedException("Can't save more than 8 courses");
		}
	}

	public void updateCourse(String name, String description, Date date, Long id) {
		Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Course [id="+id+"] not found"));

		course.setName(name);
		course.setDescription(description);
		course.setDate(date);	

		courseRepository.save(course);
	}	
	
	public void deleteCourse(Long id) throws NotDeletedException {
		Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Course [id="+id+"] not found"));
		Date currentDate = new Date(System.currentTimeMillis());

		if (currentDate.before(course.getDate())) {
			courseRepository.deleteById(id);
		}else {
			throw new NotDeletedException("Course [id="+id+"] not deleted");
		}
	}
}

package com.javadev.organizer.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.javadev.organizer.entities.Course;
import com.javadev.organizer.repositories.CourseRepository;

@RestController
public class CourseController {
	
	@Autowired
	private CourseRepository courseRepository;
	
	@GetMapping("/course/get_course/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public HttpEntity<Course> getCourseById(@PathVariable Long id) {
		
		Course course = courseRepository.findById(id).orElse(null);
		if(course != null) {
			return ResponseEntity.ok(course);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/course/show_all_courses")
	@PreAuthorize("isAuthenticated()")
	public List<Course> getAllCourses(){
		List<Course> courses = new ArrayList<>();
		courseRepository.findAll().forEach(courses::add);
		
		Collections.sort(courses, (first, second) -> first.getId().intValue() - second.getId().intValue());
		
		return courses;
	}
	
	@PostMapping("/course/create_course")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public HttpStatus saveCourse(@RequestBody Course course) {
		if(courseRepository.count()<9) {
			courseRepository.save(course);
			return HttpStatus.OK;
		}else {
			return HttpStatus.CONFLICT;
		}		
	}
	
	@PatchMapping("/course/update_course/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public void updateCourse(
			@RequestBody Course updatedCourse,
			@PathVariable Long id){
		
		//todo, need refactor
		
		Course course = courseRepository.findById(id).orElse(null);
		
		if(updatedCourse.getName()!= null && !updatedCourse.getName().equals(course.getName())) {
			course.setName(updatedCourse.getName());
		}
		
		if(updatedCourse.getDescription()!= null && !updatedCourse.getDescription().equals(course.getDescription())) {
			course.setDescription(updatedCourse.getDescription());
		}
		
		if(updatedCourse.getDate()!= null && !updatedCourse.getDate().equals(course.getDate())) {
			course.setDate(updatedCourse.getDate());
		}
		
		courseRepository.save(course);
	}
	
	@DeleteMapping("/course/remove_course/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public HttpEntity<Course> deleteCourse(@PathVariable Long id) {
		Course course = courseRepository.findById(id).orElse(null);
		Date currentDate = new Date(System.currentTimeMillis());
		
		if(currentDate.before(course.getDate())) {
			courseRepository.delete(course);
			return ResponseEntity.ok(course);
		}
		
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
}

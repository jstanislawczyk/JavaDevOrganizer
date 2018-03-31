package com.javadev.organizer.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	@GetMapping("/user/get_course/{id}")
	public HttpEntity<Course> getUserById(@PathVariable Long id) {
		
		Course course = courseRepository.findById(id).orElse(null);
		if(course != null) {
			return ResponseEntity.ok(course);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/user/show_all_courses")
	public List<Course> getAllCourses(){
		List<Course> courses = new ArrayList<>();
		courseRepository.findAll().forEach(courses::add);
		
		return courses;
	}
	
	@PostMapping("/lecturer/create_course")
	public void saveCourse(@RequestBody Course course) {
		courseRepository.save(course);
	}
	
	@PatchMapping("/lecturer/update_course/{id}")
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
	
	@DeleteMapping("/lecturer/remove_course/{id}")
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

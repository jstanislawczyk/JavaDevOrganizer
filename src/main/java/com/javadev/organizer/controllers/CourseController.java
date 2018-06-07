package com.javadev.organizer.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

import com.javadev.organizer.dto.CourseDto;
import com.javadev.organizer.dto.DtoConverter;
import com.javadev.organizer.entities.Course;
import com.javadev.organizer.services.CourseService;

@RestController
public class CourseController {

	@Autowired
	private CourseService courseService;

	@GetMapping("/api/course/{id}")
	@PreAuthorize("isAuthenticated()")
	public CourseDto getCourseById(@PathVariable Long id) {
		return DtoConverter.dtoFromCourse(courseService.getCourseById(id));
	}

	@GetMapping("/api/courses")
	@PreAuthorize("isAuthenticated()")
	public List<CourseDto> getAllCourses() {
		return courseService.getAllCourses().stream().map(course -> DtoConverter.dtoFromCourse(course)).collect(Collectors.toList());
	}
	
	@PostMapping("/api/course")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public ResponseEntity<CourseDto> saveCourse(@RequestBody @Valid CourseDto courseDto) {
		Course course = courseService.saveCourse(DtoConverter.courseFromDto(courseDto));
		
		return buildResponseEntity(DtoConverter.dtoFromCourse(course));
	}
	
	@PatchMapping("/api/course/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public void updateCourse(@RequestBody CourseDto courseDto, @PathVariable Long id) {
		courseService.updateCourse(courseDto.getName(), courseDto.getDescription(), courseDto.getDate(), id);
	}

	@DeleteMapping("/api/course/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public void deleteCourse(@PathVariable Long id) {
		courseService.deleteCourse(id);
	}
	
	private ResponseEntity<CourseDto> buildResponseEntity(CourseDto courseDto) {
		HttpHeaders header = new HttpHeaders();
		header.set("Resource-path", "/course/"+courseDto.getId());
		
		return new ResponseEntity<CourseDto>(courseDto, header, HttpStatus.CREATED);
	}
}

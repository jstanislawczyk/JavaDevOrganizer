package com.javadev.organizer.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.javadev.organizer.dto.CourseDto;
import com.javadev.organizer.dto.DtoConverter;
import com.javadev.organizer.services.CourseService;

@RestController
public class CourseController {

	@Autowired
	private CourseService courseService;

	@GetMapping("/course/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public CourseDto getCourseById(@PathVariable Long id) {
		return DtoConverter.dtoFromCourse(courseService.getCourseById(id));
	}

	@GetMapping("/courses")
	@PreAuthorize("isAuthenticated()")
	public List<CourseDto> getAllCourses() {
		return courseService.getAllCourses().stream().map(course -> DtoConverter.dtoFromCourse(course)).collect(Collectors.toList());
	}
	
	@PostMapping("/course")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public ResponseEntity<CourseDto> saveCourse(@RequestBody CourseDto courseDto, UriComponentsBuilder uriComponentsBuilder) {
		return courseService.saveCourse(DtoConverter.courseFromDto(courseDto) , uriComponentsBuilder);
	}
	
	@PatchMapping("/course/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public void updateCourse(@RequestBody CourseDto courseDto, @PathVariable Long id) {
		courseService.updateCourse(courseDto.getName(), courseDto.getDescription(), courseDto.getDate(), id);
	}

	@DeleteMapping("/course/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public void deleteCourse(@PathVariable Long id) {
		courseService.deleteCourse(id);
	}
}

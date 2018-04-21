package com.javadev.organizer.controllers;

import java.net.URI;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.javadev.organizer.entities.Course;
import com.javadev.organizer.exceptions.CourseNotCreatedException;
import com.javadev.organizer.exceptions.CourseNotFoundException;
import com.javadev.organizer.exceptions.CoursesListNotFoundException;
import com.javadev.organizer.exceptions.Error;
import com.javadev.organizer.repositories.CourseRepository;

@RestController
public class CourseController {

	@Autowired
	private CourseRepository courseRepository;

	@GetMapping("/course/get_course/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public Course getCourseById(@PathVariable Long id) {
		Course course = courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));

		return course;
	}

	@GetMapping("/course/show_all_courses")
	@PreAuthorize("isAuthenticated()")
	public List<Course> getAllCourses() {
		List<Course> courses = new ArrayList<>();
		courseRepository.findAll().forEach(courses::add);
		Collections.sort(courses, (first, second) -> first.getId().intValue() - second.getId().intValue());

		if (courses.isEmpty()) {
			throw new CoursesListNotFoundException();
		}

		return courses;
	}

	@PostMapping("/course/create_course")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public ResponseEntity<Course> saveCourse(@RequestBody Course course, UriComponentsBuilder uriComponentsBuilder) {

		if (courseRepository.count() < 8) {
			courseRepository.save(course);
			HttpHeaders headers = buildLocationHeader(String.valueOf(course.getId()), uriComponentsBuilder);
			ResponseEntity<Course> responseEntity = new ResponseEntity<>(course, headers, HttpStatus.CREATED);

			return responseEntity;
		} else {
			throw new CourseNotCreatedException();
		}
	}

	@PatchMapping("/course/update_course/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	@ResponseStatus(value = HttpStatus.OK, reason = "Course updated successfully")
	public void updateCourse(@RequestBody Course updatedCourse, @PathVariable Long id) {

		// todo, need refactor

		Course course = courseRepository.findById(id).orElse(null);

		if (updatedCourse.getName() != null && !updatedCourse.getName().equals(course.getName())) {
			course.setName(updatedCourse.getName());
		}

		if (updatedCourse.getDescription() != null && !updatedCourse.getDescription().equals(course.getDescription())) {
			course.setDescription(updatedCourse.getDescription());
		}

		if (updatedCourse.getDate() != null && !updatedCourse.getDate().equals(course.getDate())) {
			course.setDate(updatedCourse.getDate());
		}

		courseRepository.save(course);
	}

	@DeleteMapping("/course/remove_course/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Course deleted successfully")
	public void deleteCourse(@PathVariable Long id) {
		Course course = courseRepository.findById(id).orElse(null);
		Date currentDate = new Date(System.currentTimeMillis());

		if (currentDate.before(course.getDate())) {
			courseRepository.delete(course);
		}
	}

	@ExceptionHandler(CoursesListNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Error coursesListNotFound(CoursesListNotFoundException exception) {
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
	public Error courseNotCreated(CourseNotCreatedException exception) {
		return new Error(409, "Course not created. Can't create more than 8 courses");
	}

	private HttpHeaders buildLocationHeader(String value, UriComponentsBuilder uriComponentsBuilder) {
		HttpHeaders header = new HttpHeaders();
		URI locationUri = buildUri("/course/get_course/", value, uriComponentsBuilder);
		header.setLocation(locationUri);

		return header;
	}

	private URI buildUri(String path, String value, UriComponentsBuilder uriComponentsBuilder) {
		URI locationUri = uriComponentsBuilder.path(path).path(value).build().toUri();
		return locationUri;
	}
}

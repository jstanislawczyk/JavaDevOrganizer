package com.javadev.organizer.services;

import java.net.URI;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.javadev.organizer.entities.Course;
import com.javadev.organizer.exceptions.NotDeletedException;
import com.javadev.organizer.exceptions.NotFoundException;
import com.javadev.organizer.exceptions.NotSavedException;
import com.javadev.organizer.repositories.CourseRepository;

@Service
public class CourseService {
	
	@Autowired
	private CourseRepository courseRepository;

	public Course getCourseById(Long id) {
		return courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Course [id="+id+"] not found"));
	}
	
	public List<Course> getAllCourses(){
		List<Course> courses = new ArrayList<>();
		courseRepository.findAll().forEach(courses::add);
		Collections.sort(courses, (first, second) -> first.getId().intValue() - second.getId().intValue());

		if (courses.isEmpty()) {
			throw new NotFoundException("Courses list not found");
		}
		
		return courses;
	}
	
	public ResponseEntity<Course> saveCourse(Course course, UriComponentsBuilder uriComponentsBuilder) {

		if (courseRepository.count() < 8) {
			courseRepository.save(course);
			HttpHeaders header = buildLocationHeader(String.valueOf(course.getId()), uriComponentsBuilder);
			ResponseEntity<Course> responseEntity = new ResponseEntity<>(course, header, HttpStatus.CREATED);
			
			return responseEntity;
		} else {
			throw new NotSavedException("Can't save more than 8 courses");
		}
	}

	public Course updateCourse( Course updatedCourse, Long id) {
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
		
		return course;
	}	
	
	public void deleteCourse(Long id) {
		Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Course [id="+id+"] not found"));
		Date currentDate = new Date(System.currentTimeMillis());

		if (currentDate.before(course.getDate())) {
			courseRepository.deleteById(id);
		}else {
			throw new NotDeletedException("Course [id="+id+"] not deleted");
		}
	}
	
	private HttpHeaders buildLocationHeader(String value, UriComponentsBuilder uriComponentsBuilder) {
		HttpHeaders header = new HttpHeaders();
		URI locationUri = buildUri("/course/", value, uriComponentsBuilder);
		header.setLocation(locationUri);

		return header;
	}

	private URI buildUri(String path, String value, UriComponentsBuilder uriComponentsBuilder) {
		URI locationUri = uriComponentsBuilder.path(path).path(value).build().toUri();
		return locationUri;
	}
}

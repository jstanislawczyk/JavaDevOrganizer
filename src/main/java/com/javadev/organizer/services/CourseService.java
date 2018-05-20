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

import com.javadev.organizer.dto.CourseDto;
import com.javadev.organizer.dto.DtoConverter;
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
	
	public ResponseEntity<CourseDto> saveCourse(Course course, UriComponentsBuilder uriComponentsBuilder) throws NotSavedException {

		if (courseRepository.count() < 8) {
			courseRepository.save(course);
			HttpHeaders header = buildLocationHeader(String.valueOf(course.getId()), uriComponentsBuilder);
			CourseDto courseDto = DtoConverter.dtoFromCourse(course);
			
			return new ResponseEntity<>(courseDto, header, HttpStatus.CREATED);
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

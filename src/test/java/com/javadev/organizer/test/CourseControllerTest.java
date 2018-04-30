package com.javadev.organizer.test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadev.organizer.controllers.CourseController;
import com.javadev.organizer.entities.Course;
import com.javadev.organizer.exceptions.CourseNotFoundException;
import com.javadev.organizer.exceptions.CoursesListNotFoundException;
import com.javadev.organizer.exceptions.handlers.GlobalCourseExceptionsHandler;
import com.javadev.organizer.repositories.CourseRepository;
import com.javadev.organizer.services.CourseService;

public class CourseControllerTest {

	private MockMvc mockMvc;
	
	@Mock
	private CourseService courseService;
	
	@Mock
	private CourseRepository courseRepository;
	
	@InjectMocks
	private CourseController courseController;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);   
		mockMvc = MockMvcBuilders.standaloneSetup(courseController).setControllerAdvice(new GlobalCourseExceptionsHandler()).build();
	}
	
	@Test
	public void shouldSaveCourse() throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();	
		Course unsavedCourse = new Course.Builder().name("Java").description("Spring")	.build();
		Course savedCourse =  new Course.Builder().id(1L).name("Java").description("Spring").build();	
		String json = mapper.writeValueAsString(unsavedCourse);
		
		when(courseRepository.count()).thenReturn(4L);
		when(courseRepository.save(unsavedCourse)).thenReturn(savedCourse);
		
		mockMvc.perform(post("/course").contentType( MediaType.APPLICATION_JSON).content(json))
			   .andExpect(status().is2xxSuccessful());
	}

	@Test
	public void shouldFindCourseById() throws Exception {
		Course expectedCourse = new Course.Builder().id(1L).name("Java").description("Spring").build();
		when(courseService.getCourseById(1L)).thenReturn(expectedCourse);
		
		mockMvc.perform(get("/course/1")).andExpect(status().isOk());
	}
	
	@Test
	public void shouldNotFindCourseById() throws Exception {
		when(courseService.getCourseById(1L)).thenThrow(CourseNotFoundException.class);
		
		mockMvc.perform(get("/course/1")).andExpect(status().isNotFound());
	}
	
	@Test
	public void shouldFindAllCoursesList() throws Exception {
		when(courseService.getAllCourses()).thenReturn(expectedCourses());
		
		mockMvc.perform(get("/courses")).andExpect(status().isOk());
	}
	
	@Test
	public void shouldFindEmptyCoursesList() throws Exception {
		when(courseService.getAllCourses().isEmpty()).thenThrow(CoursesListNotFoundException.class);
		
		mockMvc.perform(get("/courses")).andExpect(status().isNotFound());
	}
	
	private List<Course> expectedCourses(){
		List<Course> courses = new ArrayList<>();	
		courses.add(new Course.Builder().id(1L).name("Java Basics").description("Java").build());
		courses.add(new Course.Builder().id(2L).name("Spring Basics").description("Spring").build());
		
		return courses;
	}
}

package com.javadev.organizer.test;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.javadev.organizer.repositories.CourseRepository;

public class CourseControllerTest {

	private MockMvc mockMvc;
	
	@Mock
	private CourseRepository courseRepository;
	
	@InjectMocks
	private CourseController courseController;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);   
		mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
	}
	
	@Test
	public void testSaveCourse() throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();	
		Course unsavedCourse = new Course.Builder().name("Java").description("Spring")	.build();
		Course savedCourse =  new Course.Builder().id(1L).name("Java").description("Spring").build();	
		String json = mapper.writeValueAsString(unsavedCourse);
		
		when(courseRepository.count()).thenReturn(4L);
		when(courseRepository.save(unsavedCourse)).thenReturn(savedCourse);
		
		mockMvc
			.perform(post("/course/create_course")
					.contentType( MediaType.APPLICATION_JSON).content(json))
			.andExpect(status().isCreated());
		
		verify(courseRepository, atLeastOnce()).save(unsavedCourse);
	}
}

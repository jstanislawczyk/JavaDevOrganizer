package com.javadev.organizer.test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.javadev.organizer.controllers.StudentController;
import com.javadev.organizer.exceptions.handlers.GlobalUserExceptionsHandler;
import com.javadev.organizer.repositories.CourseRepository;
import com.javadev.organizer.repositories.UserRepository;
import com.javadev.organizer.services.StudentService;

public class StudentControllerTest {

	private MockMvc mockMvc;
	
	@Mock
	private CourseRepository courseRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private StudentService studentService;
	
	@InjectMocks
	private StudentController studentController;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(studentController).setControllerAdvice(new GlobalUserExceptionsHandler()).build();
	}
	
	@Test
	public void shouldReturnCoursesIds() throws Exception {
		List<BigDecimal> expectedCoursesIds = new ArrayList<>();
		expectedCoursesIds.add(new BigDecimal(new BigInteger("1")));
		expectedCoursesIds.add(new BigDecimal(new BigInteger("5")));
		expectedCoursesIds.add(new BigDecimal(new BigInteger("6")));
		
		when(studentService.getCoursesIdsByUser(1L)).thenReturn(ResponseEntity.ok(expectedCoursesIds));
		
		mockMvc.perform(get("/student/{id}/courses/ids", 1L))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
	@Test
	public void shouldReturnCoursesIdsNotFound() throws Exception {
		when(studentService.getCoursesIdsByUser(1L)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		
		mockMvc.perform(get("/student/checkCoursesStatus/{id}", 1L)).andExpect(status().isNotFound());
	}
}
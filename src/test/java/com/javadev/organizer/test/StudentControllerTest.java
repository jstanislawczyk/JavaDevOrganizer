package com.javadev.organizer.test;

import static org.mockito.Mockito.mock;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.javadev.organizer.controllers.StudentController;
import com.javadev.organizer.repositories.CourseRepository;
import com.javadev.organizer.repositories.UserRepository;

public class StudentControllerTest {

	private MockMvc mockMvc;
	private StudentController studentController;
	private CourseRepository courseRepository;
	private UserRepository userRepository;
	
	@Before
	public void setup(){
		courseRepository = mock(CourseRepository.class);
		userRepository = mock(UserRepository.class);
		
		studentController = new StudentController(userRepository, courseRepository);
		
		mockMvc = MockMvcBuilders
				.standaloneSetup(studentController)
				.build();
	}
	
	@Test
	public void testIdList() throws Exception {
		List<BigDecimal> expectedCoursesIds = new ArrayList<>();
		expectedCoursesIds.add(new BigDecimal(new BigInteger("1")));
		expectedCoursesIds.add(new BigDecimal(new BigInteger("5")));
		expectedCoursesIds.add(new BigDecimal(new BigInteger("6")));
		
		when(courseRepository.findCoursesIdsByUserId(1L)).thenReturn(expectedCoursesIds);
		
		mockMvc
			.perform(get("/student/checkCoursesStatus/{id}", 1L))
			.andExpect(status().isOk())
		    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
	@Test
	public void testEmptyIdList() throws Exception{
		when(courseRepository.findCoursesIdsByUserId(1L)).thenReturn(new ArrayList<>());
		
		mockMvc
			.perform(get("/student/checkCoursesStatus/{id}", 1L))
			.andExpect(status().isNotFound());
	}
}
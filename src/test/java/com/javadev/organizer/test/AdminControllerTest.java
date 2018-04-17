package com.javadev.organizer.test;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

import com.javadev.organizer.controllers.AdminController;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.repositories.UserRepository;

public class AdminControllerTest {

	private MockMvc mockMvc;
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private AdminController adminController;
	
	@Before
	public void setup() {
	    MockitoAnnotations.initMocks(this);
	    
		mockMvc = MockMvcBuilders
				.standaloneSetup(adminController)
				.build();
	}
	
	@Test
	public void testGetUserList() throws Exception {
		List<User> expectedUsers = getExpectedUsers();
		
		when(userRepository.findAll()).thenReturn(expectedUsers);
		
		mockMvc
			.perform(get("/admin_control/find_all_users"))
			.andExpect(status().isOk())
		    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].email", is("jkowalski@mail.com")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].email", is("anowak@mail.com")));
	}
	
	@Test
	public void testGetEmptyUserList() throws Exception{
		when(userRepository.findAll()).thenReturn(new ArrayList<>());
		
		mockMvc
			.perform(get("/admin_control/find_all_users"))
			.andExpect(status().isNotFound());
	}
	
	private List<User> getExpectedUsers() {
		List<User> expectedUsers = new ArrayList<>();
		expectedUsers.add(
				new User.Builder()
				.id(1L)
				.email("jkowalski@mail.com")
				.build());
		
		expectedUsers.add(
				new User.Builder()
				.id(2L)
				.email("anowak@mail.com")
				.build());
		
		return expectedUsers;
	}
}

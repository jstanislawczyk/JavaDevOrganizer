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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.javadev.organizer.controllers.LecturerController;
import com.javadev.organizer.entities.Role;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.exceptions.NotFoundException;
import com.javadev.organizer.exceptions.handlers.GlobalExceptionHandler;
import com.javadev.organizer.repositories.UserRepository;
import com.javadev.organizer.services.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class LecturerControllerTest {
	
	private MockMvc mockMvc;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private UserService userService;
	
	@InjectMocks
	private LecturerController lecturerController;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(lecturerController).setControllerAdvice(new GlobalExceptionHandler()).build();
	}
	
	@Test
	public void shouldFindUserById() throws Exception {
		User expectedUser = new User.Builder().email("test@mail.com").firstName("Jan").lastName("Kowalski").role(Role.STUDENT).build();
		when(userService.getUserById(1L)).thenReturn(expectedUser);
		
		mockMvc.perform(get("/api/user/1")).andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "lecturer@gmail.com", authorities = { "LECTURER","USER" })
	public void shouldNotFindUserById() throws Exception {
		when(userService.getUserById(1L)).thenThrow(new NotFoundException("User [id=1] not found"));
		
		mockMvc.perform(get("/api/user/1")).andExpect(status().isNotFound());
	}
	
	@Test
	public void shouldReturnStudentList() throws Exception {
		List<User> expectedStudents = getExpectedUsers();

		when(userService.getAllUsersByRole("STUDENT")).thenReturn(expectedStudents);

		mockMvc.perform(get("/api/users?role=STUDENT")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$[0].id", is(1))).andExpect(jsonPath("$[0].email", is("jkowalski@mail.com"))).andExpect(jsonPath("$[0].role", is("STUDENT")))
				.andExpect(jsonPath("$[1].id", is(2))).andExpect(jsonPath("$[1].email", is("anowak@mail.com"))).andExpect(jsonPath("$[0].role", is("STUDENT")));
	}
	
	private List<User> getExpectedUsers() {
		List<User> expectedUsers = new ArrayList<>();
		expectedUsers.add(new User.Builder().id(1L).email("jkowalski@mail.com").role(Role.STUDENT).build());
		expectedUsers.add(new User.Builder().id(2L).email("anowak@mail.com").role(Role.STUDENT).build());

		return expectedUsers;
	}
}

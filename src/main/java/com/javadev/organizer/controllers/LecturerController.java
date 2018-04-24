package com.javadev.organizer.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.javadev.organizer.entities.Role;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.exceptions.UserNotFoundException;
import com.javadev.organizer.exceptions.UsersListNotFoundException;
import com.javadev.organizer.repositories.UserRepository;

@RestController
public class LecturerController {

	private PasswordEncoder passwordEncoder;
	private UserRepository userRepository;

	@Autowired
	public LecturerController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping("/lecturer/users/students")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public List<User> getAllStudents() {
		List<User> users = userRepository.findByRole(Role.STUDENT.name());

		if (users.isEmpty()) {
			throw new UsersListNotFoundException();
		} 
		
		return users;
	}

	@GetMapping("/lecturer/user/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public User getUserById(@PathVariable Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		
		return user;
	}

	@PostMapping("/lecturer/user/student")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public ResponseEntity<User> saveStudent(@RequestBody User student, UriComponentsBuilder uriComponentsBuilder) {
		setupStudent(student);
		userRepository.save(student);
		
		HttpHeaders headers = buildLocationHeader(String.valueOf(student.getId()), uriComponentsBuilder);
		ResponseEntity<User> responseEntity = new ResponseEntity<>(student, headers, HttpStatus.CREATED);
		
		return responseEntity;	
	}

	@PatchMapping("/lecturer/user/student/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public void updateStudent(@RequestBody User updatedStudent, @PathVariable Long id) {

		// need refactor, todo

		User student = userRepository.findById(id).orElse(null);

		if (updatedStudent.getFirstName() != null && !updatedStudent.getFirstName().equals(student.getFirstName())) {
			student.setFirstName(updatedStudent.getFirstName());
		}

		if (updatedStudent.getLastName() != null && !updatedStudent.getLastName().equals(student.getLastName())) {
			student.setLastName(updatedStudent.getLastName());
		}

		if (updatedStudent.getEmail() != null && !updatedStudent.getEmail().equals(student.getEmail())) {
			student.setEmail(updatedStudent.getEmail());
		}

		userRepository.save(student);
	}

	@DeleteMapping("/lecturer/user/student/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public void deleteStudent(@PathVariable Long id) {
		userRepository.deleteById(id);
	}

	private User setupStudent(User student) {
		student.setRole(Role.STUDENT.name());
		student.setPassword(passwordEncoder.encode(student.getPassword()));

		return student;
	}
	
	private HttpHeaders buildLocationHeader(String value, UriComponentsBuilder uriComponentsBuilder) {
		HttpHeaders header = new HttpHeaders();
		URI locationUri = buildUri("/lecturer/show_user_by_id/", value, uriComponentsBuilder);
		header.setLocation(locationUri);

		return header;
	}

	private URI buildUri(String path, String value, UriComponentsBuilder uriComponentsBuilder) {
		URI locationUri = uriComponentsBuilder.path(path).path(value).build().toUri();
		return locationUri;
	}
}

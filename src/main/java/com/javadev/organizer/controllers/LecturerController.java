package com.javadev.organizer.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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

import com.javadev.organizer.entities.Role;
import com.javadev.organizer.entities.User;
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
	
	@GetMapping("/lecturer/find_all_students")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public HttpEntity<List<User>> getAllStudents() {
		List<User> users = userRepository.findByRole(Role.STUDENT.name());
		
		if(users.isEmpty()) {
			return ResponseEntity.ok(users);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/lecturer/show_user_by_id/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public HttpEntity<User> getUserById(@PathVariable Long id) {
		
		User user = userRepository.findById(id).orElse(null);
		
		if(user != null) {
			return ResponseEntity.ok(user);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/lecturer/create_student")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public void saveStudent(@RequestBody User student) {
		setupStudent(student);	
		
		userRepository.save(student);
	}
	
	@PatchMapping("/lecturer/update_student/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public void updateStudent(
			@RequestBody User updatedStudent, 
			@PathVariable Long id) {
		
		//need refactor, todo
		
		User student = userRepository.findById(id).orElse(null);
		
		if(updatedStudent.getFirstName()!= null && !updatedStudent.getFirstName().equals(student.getFirstName())) {
			student.setFirstName(updatedStudent.getFirstName());
		}
		
		if(updatedStudent.getLastName()!= null && !updatedStudent.getLastName().equals(student.getLastName())) {
			student.setLastName(updatedStudent.getLastName());
		}
		
		if(updatedStudent.getEmail()!= null && !updatedStudent.getEmail().equals(student.getEmail())) {
			student.setEmail(updatedStudent.getEmail());
		}
		
		userRepository.save(student);
	}
	
	@DeleteMapping("/lecturer/remove_student/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public void deleteStudent(@PathVariable Long id) {
		userRepository.deleteById(id);
	}
	
	private User setupStudent(User student) {
		student.setRole(Role.STUDENT.name());
		student.setPassword(passwordEncoder.encode(student.getPassword()));
		
		return student;
	}
}

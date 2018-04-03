package com.javadev.organizer.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	public List<User> getAllStudents() {
		return userRepository.findByRole(Role.STUDENT.name());
	}
	
	@PostMapping("/lecturer/create_student")
	public void saveStudent(@RequestBody User student) {
		setupStudent(student);	
		
		userRepository.save(student);
	}
	
	@PatchMapping("/lecturer/update_student/{id}")
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
	public void deleteStudent(@PathVariable Long id) {
		userRepository.deleteById(id);
	}
	
	private User setupStudent(User student) {
		student.setRole(Role.STUDENT.name());
		student.setPassword(passwordEncoder.encode(student.getPassword()));
		
		return student;
	}
}

package com.javadev.organizer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.javadev.organizer.entities.Role;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.repositories.UserRepository;

@RestController
public class LecturerController {

	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/lecturer/save_student")
	public void saveStudent(@RequestBody User student) {
		student.setRole(Role.STUDENT.name());
		
		userRepository.save(student);
	}
}

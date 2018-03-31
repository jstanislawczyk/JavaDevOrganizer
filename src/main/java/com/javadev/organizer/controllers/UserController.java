package com.javadev.organizer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.javadev.organizer.entities.User;
import com.javadev.organizer.repositories.UserRepository;

@RestController
public class UserController {
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/user/show_user_by_id/{id}")
	public HttpEntity<User> getUserById(@PathVariable Long id) {
		
		User user = userRepository.findById(id).orElse(null);
		if(user != null) {
			return ResponseEntity.ok(user);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}

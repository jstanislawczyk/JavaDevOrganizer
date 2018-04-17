package com.javadev.organizer.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javadev.organizer.entities.User;
import com.javadev.organizer.repositories.UserRepository;

@RestController
public class AdminController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/admin_control/find_all_users")
	@PreAuthorize("hasAuthority('ADMIN')")
	public HttpEntity<List<User>> getAllUsers(){
		List<User> users = new ArrayList<>();
		userRepository.findAll().forEach(users::add);

		if(users.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			return ResponseEntity.ok(users);
		}
	}
}

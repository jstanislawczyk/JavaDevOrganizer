package com.javadev.organizer.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javadev.organizer.entities.User;
import com.javadev.organizer.repositories.UserRepository;

@RestController
public class AdminController {
	
	@Autowired
	private UserRepository UserRepository;
	
	@GetMapping("/admin_control/find_all_users")
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<User> getAllUsers(){
		List<User> users = new ArrayList<>();
		UserRepository.findAll().forEach(users::add);
		
		return users;
	}
}

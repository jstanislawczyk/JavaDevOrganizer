package com.javadev.organizer.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.javadev.organizer.entities.User;
import com.javadev.organizer.services.AdminService;

@RestController
public class AdminController {

	@Autowired
	private AdminService adminService;

	@GetMapping("/admin/users")
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<User> getAllUsers() {
		return adminService.getAllUsers();
	}

	@PostMapping("/admin/user")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<User> saveUser(@RequestBody User user, UriComponentsBuilder uriComponentsBuilder) {
		return adminService.saveUser(user, uriComponentsBuilder);
	}
}

package com.javadev.organizer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.javadev.organizer.entities.User;
import com.javadev.organizer.repositories.UserRepository;

/*
 * This temporary class is used to find user by email and check password correctness.
 * Methods are used by simple JSClient (https://github.com/xenonso/JavaDevOrganizer-JSClient).
 * It's not safe and will be deleted when my JS client will support session control
 */
@RestController
public class TemporaryClassForUserAuth {

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/auth/checkUserData")
	@PreAuthorize("isAuthenticated()")
	public HttpEntity<User> getUserIfExists(@RequestBody User user) {
		User authenticatedUser = userRepository.findByEmail(user.getEmail()).orElse(null);

		if (authenticatedUser != null) {
			return ResponseEntity.ok(authenticatedUser);
		}

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
}

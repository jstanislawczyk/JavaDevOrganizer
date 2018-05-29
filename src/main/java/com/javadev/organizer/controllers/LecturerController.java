package com.javadev.organizer.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.javadev.organizer.dto.DtoConverter;
import com.javadev.organizer.dto.UserDto;
import com.javadev.organizer.services.UserService;

@RestController
public class LecturerController {

	@Autowired
	private UserService userService;

	@GetMapping("/api/user/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public UserDto getUserById(@PathVariable Long id) {
		return DtoConverter.dtoFromUser(userService.getUserById(id));
	}
	
	@GetMapping(value = "/api/users", params = "role=STUDENT")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public List<UserDto> getAllStudents() {
		return userService.getAllUsersByRole("STUDENT").stream().map(user -> DtoConverter.dtoFromUser(user)).collect(Collectors.toList());
	}
}

package com.javadev.organizer.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.javadev.organizer.dto.DtoConverter;
import com.javadev.organizer.dto.UserDto;
import com.javadev.organizer.services.AdminService;

@RestController
public class AdminController {

	@Autowired
	private AdminService adminService;

	@GetMapping("/admin/users")
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<UserDto> getAllUsers() {
		return adminService.getAllUsers().stream().map(user -> DtoConverter.dtoFromUser(user)).collect(Collectors.toList());
	}

	@PostMapping("/admin/user")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto, UriComponentsBuilder uriComponentsBuilder) {
		return adminService.saveUser(DtoConverter.userFromDto(userDto), uriComponentsBuilder);
	}
	
	@DeleteMapping("/admin/user")
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteUser(Long id) {
		adminService.deleteUser(id);
	}
}

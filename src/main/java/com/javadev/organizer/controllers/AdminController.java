package com.javadev.organizer.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@PatchMapping("/admin/user/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public void updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
		adminService.updateUser(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), id);
	}
	
	@DeleteMapping("/admin/user/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteUser(@PathVariable Long id) {
		adminService.deleteUser(id);
	}
}

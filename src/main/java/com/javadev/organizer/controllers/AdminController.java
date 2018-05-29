package com.javadev.organizer.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javadev.organizer.dto.DtoConverter;
import com.javadev.organizer.dto.UserDto;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.exceptions.NotFoundException;
import com.javadev.organizer.services.UserService;

@RestController
public class AdminController {

	@Autowired
	private UserService userService;

	@GetMapping(value = "/api/users", params = "role=ALL")
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<UserDto> getAllUsers() {
		return userService.getAllUsers().stream().map(user -> DtoConverter.dtoFromUser(user)).collect(Collectors.toList());
	}
	
	@GetMapping(value = "/api/users", params = {"role!=ALL", "role!=STUDENT"})
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<UserDto> getAllUsersByRole(@RequestParam(name="role") String role) {
		return userService.getAllUsersByRole(role).stream().map(user -> DtoConverter.dtoFromUser(user)).collect(Collectors.toList());
	}

	@PostMapping("/api/user")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) {
		User user = userService.saveUser(DtoConverter.userFromDto(userDto));
		return buildResponseEntity(DtoConverter.dtoFromUser(user));
	}
	
	@PatchMapping("/api/user/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public void updateUser(@RequestBody UserDto userDto, @PathVariable Long id) throws NotFoundException{
		userService.updateUser(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), id);
	}
	
	@DeleteMapping("/api/user/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
	}
	
	private ResponseEntity<UserDto> buildResponseEntity(UserDto userDto) {
		HttpHeaders header = new HttpHeaders();
		header.set("Resource-path", "/api/user/"+userDto.getId());
		
		return new ResponseEntity<UserDto>(userDto, header, HttpStatus.CREATED);
	}
}

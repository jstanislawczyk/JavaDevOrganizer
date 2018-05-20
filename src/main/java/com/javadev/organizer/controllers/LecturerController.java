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
import com.javadev.organizer.services.LecturerService;

@RestController
public class LecturerController {

	@Autowired
	private LecturerService lecturerService;

	@GetMapping("/lecturer/users/students")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public List<UserDto> getAllStudents() {
		return lecturerService.getAllStudents().stream().map(user -> DtoConverter.dtoFromUser(user)).collect(Collectors.toList());
	}

	@GetMapping("/lecturer/user/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public UserDto getUserById(@PathVariable Long id) {
		return DtoConverter.dtoFromUser(lecturerService.getUserById(id));
	}

	@PostMapping("/lecturer/user/student")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public ResponseEntity<UserDto> saveStudent(@RequestBody UserDto studentDto, UriComponentsBuilder uriComponentsBuilder) {
		return lecturerService.saveStudent(DtoConverter.userFromDto(studentDto), uriComponentsBuilder);
	}

	@PatchMapping("/lecturer/user/student/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public void updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
		lecturerService.updateUser(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), id);
	}

	@DeleteMapping("/lecturer/user/student/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public void deleteStudent(@PathVariable Long id) {
		lecturerService.deleteStudent(id);
	}
}

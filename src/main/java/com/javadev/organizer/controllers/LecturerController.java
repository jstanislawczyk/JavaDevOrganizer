package com.javadev.organizer.controllers;

import java.util.List;

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

import com.javadev.organizer.entities.User;
import com.javadev.organizer.services.LecturerService;

@RestController
public class LecturerController {

	@Autowired
	private LecturerService lecturerService;

	@GetMapping("/lecturer/users/students")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public List<User> getAllStudents() {
		return lecturerService.getAllStudents();
	}

	@GetMapping("/lecturer/user/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public User getUserById(@PathVariable Long id) {
		return lecturerService.getUserById(id);
	}

	@PostMapping("/lecturer/user/student")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public ResponseEntity<User> saveStudent(@RequestBody User student, UriComponentsBuilder uriComponentsBuilder) {
		return lecturerService.saveStudent(student, uriComponentsBuilder);
	}

	@PatchMapping("/lecturer/user/student/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public void updateStudent(@RequestBody User updatedStudent, @PathVariable Long id) {
		lecturerService.updateStudent(updatedStudent, id);
	}

	@DeleteMapping("/lecturer/user/student/{id}")
	@PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
	public void deleteStudent(@PathVariable Long id) {
		lecturerService.deleteStudent(id);
	}
}

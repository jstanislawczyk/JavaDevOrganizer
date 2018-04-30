package com.javadev.organizer.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javadev.organizer.services.StudentService;

@RestController
public class StudentController {

	@Autowired
	private StudentService studentService;

	@PostMapping("/student/register-presence")
	@PreAuthorize("isAuthenticated()")
	public void registerUserPresence(
			@RequestParam(value = "courseId", required = true) Long courseId,
			@RequestParam(value = "userId", required = true) Long userId) {

		studentService.registerUserPresence(courseId, userId);
	}

	@GetMapping("/student/{id}/courses/ids")
	@PreAuthorize("isAuthenticated()")
	public HttpEntity<List<BigDecimal>> getCoursesIdsByUser(@PathVariable Long id) {
		return studentService.getCoursesIdsByUser(id);
	}
}

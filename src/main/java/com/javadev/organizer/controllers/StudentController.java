package com.javadev.organizer.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javadev.organizer.security.JwtUserDetails;
import com.javadev.organizer.services.StudentService;

@RestController
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	@GetMapping("/api/me")
	@PreAuthorize("isAuthenticated()")
	public JwtUserDetails getCurrentUser() {
		return (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@PostMapping("/api/student/{userId}/courses/{courseId}")
	@PreAuthorize("isAuthenticated()")
	public Long registerUserPresence(@PathVariable Long userId, @PathVariable Long courseId, @RequestParam(value = "present", required = true) boolean present) {
		return studentService.registerUserPresence(courseId, userId, present);
	}

	@GetMapping("/api/student/{id}/courses/")
	@PreAuthorize("isAuthenticated()")
	public Map<Long, Boolean> getCoursesStatusByUserId(@PathVariable Long id) {
		return studentService.getCoursesStatusByUserId(id);
	}
}

package com.javadev.organizer.services;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.javadev.organizer.entities.Role;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.exceptions.NotFoundException;
import com.javadev.organizer.exceptions.NotUniqueException;
import com.javadev.organizer.repositories.UserRepository;

@Service
public class LecturerService {

	private PasswordEncoder passwordEncoder;
	private UserRepository userRepository;

	@Autowired
	public LecturerService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public List<User> getAllStudents() {
		List<User> users = userRepository.findByRole(Role.STUDENT);

		if (users.isEmpty()) {
			throw new NotFoundException("Students list not found");
		}

		return users;
	}

	public User getUserById(@PathVariable Long id) throws NotFoundException {
		User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User [id="+id+"] not found"));

		return user;
	}

	public ResponseEntity<User> saveStudent(@RequestBody User student, UriComponentsBuilder uriComponentsBuilder) throws NotUniqueException {
		
		setupStudent(student);

		if (isEmailUnique(student.getEmail())) {
			userRepository.save(student);
			HttpHeaders headers = buildLocationHeader(String.valueOf(student.getId()), uriComponentsBuilder);
			ResponseEntity<User> responseEntity = new ResponseEntity<>(student, headers, HttpStatus.CREATED);

			return responseEntity;
		} else {
			throw new NotUniqueException("Email already exists");
		}
	}

	public void updateStudent(@RequestBody User updatedStudent, @PathVariable Long id) {

		// need refactor, todo

		User student = userRepository.findById(id).orElse(null);

		if (updatedStudent.getFirstName() != null && !updatedStudent.getFirstName().equals(student.getFirstName())) {
			student.setFirstName(updatedStudent.getFirstName());
		}

		if (updatedStudent.getLastName() != null && !updatedStudent.getLastName().equals(student.getLastName())) {
			student.setLastName(updatedStudent.getLastName());
		}

		if (isEmailUnique(updatedStudent.getEmail()) && updatedStudent.getEmail() != null && !updatedStudent.getEmail().equals(student.getEmail())) {
			student.setEmail(updatedStudent.getEmail());
		}

		userRepository.save(student);
	}

	public void deleteStudent(@PathVariable Long id) {
		userRepository.deleteById(id);
	}

	private User setupStudent(User student) {
		student.setRole(Role.STUDENT);
		student.setPassword(passwordEncoder.encode(student.getPassword()));

		return student;
	}

	private boolean isEmailUnique(String email) {
		return (userRepository.countByEmail(email) == 0) ? true : false;
	}

	private HttpHeaders buildLocationHeader(String value, UriComponentsBuilder uriComponentsBuilder) {
		HttpHeaders header = new HttpHeaders();
		URI locationUri = buildUri("/lecturer/user/", value, uriComponentsBuilder);
		header.setLocation(locationUri);

		return header;
	}

	private URI buildUri(String path, String value, UriComponentsBuilder uriComponentsBuilder) {
		URI locationUri = uriComponentsBuilder.path(path).path(value).build().toUri();
		return locationUri;
	}
}

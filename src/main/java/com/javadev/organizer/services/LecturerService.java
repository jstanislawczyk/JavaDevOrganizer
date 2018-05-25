package com.javadev.organizer.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.javadev.organizer.entities.Role;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.exceptions.NotDeletedException;
import com.javadev.organizer.exceptions.NotFoundException;
import com.javadev.organizer.exceptions.NotUniqueException;
import com.javadev.organizer.exceptions.NotUpdatedException;
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
		
		return users;
	}

	public User getUserById(Long id) throws NotFoundException {
		User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User [id="+id+"] not found"));

		return user;
	}

	public User saveStudent(@RequestBody User student) throws NotUniqueException {
		if (isEmailUnique(student.getEmail())) {
			setupStudent(student);
			userRepository.save(student);

			return student;
		} else {
			throw new NotUniqueException("Email already exists");
		}
	}

	public void updateStudent(String firstName, String lastName, String email, Long id) throws NotFoundException, NotUpdatedException {
		User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User [id="+id+"] not found"));
		
		if(user.getRole() != Role.STUDENT) {
			throw new NotUpdatedException("Lecturer can only update student");
		}
		
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		
		userRepository.save(user);
	}

	public void deleteStudent(Long id) throws NotFoundException, NotDeletedException{
		User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User [id="+id+"] not found"));
		
		if (user.getRole() != Role.STUDENT) {
            throw new NotDeletedException("Lecturer can delete only students");
        }
		
		userRepository.deleteById(id);;
	}

	private User setupStudent(User student) {
		student.setRole(Role.STUDENT);
		student.setPassword(passwordEncoder.encode(student.getPassword()));

		return student;
	}

	private boolean isEmailUnique(String email) {
		return (userRepository.countByEmail(email) == 0) ? true : false;
	}
}

package com.javadev.organizer.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.javadev.organizer.entities.Role;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.exceptions.NotFoundException;
import com.javadev.organizer.exceptions.NotUniqueException;
import com.javadev.organizer.repositories.UserRepository;

@Service
public class AdminService {
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public List<User> getAllUsers(){
		List<User> users = new ArrayList<>();
		userRepository.findAll().forEach(users::add);

		return users;
	}

	public User saveUser(@RequestBody User user) throws NotUniqueException {
		if (isEmailUnique(user.getEmail())) {
			setupUser(user);
			userRepository.save(user);
			
			return user;
		} else {
			throw new NotUniqueException("Email already exists");
		}
	}
	
	public void updateUser(String firstName, String lastName, String email, Long id) throws NotFoundException {
		User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User [id="+id+"] not found"));
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		
		userRepository.save(user);
	}
	
	public void deleteUser(Long id){	
		userRepository.deleteById(id);;
	}

	private void setupUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		validRole(user);
	}

	private void validRole(User user) {

		if (!EnumUtils.isValidEnum(Role.class, user.getRole().name())) {
			user.setRole(Role.STUDENT);
		}
	}

	private boolean isEmailUnique(String email) {
		return (userRepository.countByEmail(email) == 0) ? true : false;
	}
}

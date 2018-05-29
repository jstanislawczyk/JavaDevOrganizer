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
public class UserService {

	private PasswordEncoder passwordEncoder;
	private UserRepository userRepository;
	
	@Autowired
	public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}
	
	public User getUserById(Long id) throws NotFoundException {
		User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User [id="+id+"] not found"));

		return user;
	}
	
	public List<User> getAllUsers(){
		List<User> users = new ArrayList<>();
		userRepository.findAll().forEach(users::add);

		return users;
	}
	
	public List<User> getAllUsersByRole(String role){
		validRole(role);
		return userRepository.findByRole(Role.valueOf(role));
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
		userRepository.deleteById(id);
	}

	private void setupUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		validRole(user.getRole().name());
	}

	private void validRole(String role) throws NotFoundException {
		if (!EnumUtils.isValidEnum(Role.class, role)) {
			throw new NotFoundException("Wrong role");
		}
	}

	private boolean isEmailUnique(String email) {
		return (userRepository.countByEmail(email) == 0) ? true : false;
	}
}

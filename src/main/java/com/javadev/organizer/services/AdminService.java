package com.javadev.organizer.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.javadev.organizer.dto.DtoConverter;
import com.javadev.organizer.dto.UserDto;
import com.javadev.organizer.entities.Role;
import com.javadev.organizer.entities.User;
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

	public ResponseEntity<UserDto> saveUser(@RequestBody User user, UriComponentsBuilder uriComponentsBuilder) throws NotUniqueException {
		
		setupUser(user);

		if (isEmailUnique(user.getEmail())) {
			userRepository.save(user);
			UserDto userDto = DtoConverter.dtoFromUser(user);
			HttpHeaders headers = buildLocationHeader(String.valueOf(user.getId()), uriComponentsBuilder);

			return new ResponseEntity<>(userDto, headers, HttpStatus.CREATED);
		} else {
			throw new NotUniqueException("Email already exists");
		}
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

package com.javadev.organizer.config;

import java.nio.CharBuffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.javadev.organizer.entities.Role;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.repositories.UserRepository;

@Component
public class InitialAdminAccountSetup {

	private PasswordEncoder passwordEncoder;
	private UserRepository userRepository;

	@Autowired
	public InitialAdminAccountSetup(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void createFirstAdminAccount() {

		if (userRepository.findById(1L).orElse(null) == null) {
			User admin = new User.Builder().firstName("admin").lastName("admin").email("admin@gmail.com")
					.password("admin".toCharArray()).role(Role.ADMIN).build();
			admin.setPassword(passwordEncoder.encode(CharBuffer.wrap(admin.getPassword())).toCharArray());

			userRepository.save(admin);
		}
	}
}

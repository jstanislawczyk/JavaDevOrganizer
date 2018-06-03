package com.javadev.organizer.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.javadev.organizer.entities.User;
import com.javadev.organizer.exceptions.NotFoundException;
import com.javadev.organizer.exceptions.TokenErrorException;
import com.javadev.organizer.repositories.UserRepository;

@Service
public class TokenService {

	private JwtGenerator jwtGenerator;
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

    public TokenService(JwtGenerator jwtGenerator, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtGenerator = jwtGenerator;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public String generateAuthToken(final User givenUser) {
    	if(isUserValid(givenUser)) {
    		 return jwtGenerator.generate(givenUser);
    	} else {
    		throw new TokenErrorException("User details not valid");
    	}   		
    }
    
    
    private boolean isUserValid(User givenUser) {
    	User user = userRepository.findByEmail(givenUser.getEmail()).orElseThrow( () -> new NotFoundException("User not found [email=" +givenUser.getEmail()+ "]"));
    	
    	if(isPasswordCorrect(user, givenUser)) {
    		updateUserAuthorities(givenUser, user);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    private boolean isPasswordCorrect(User user, User givenUser) { 	
    	if(passwordEncoder.matches(String.valueOf(givenUser.getPassword()), String.valueOf(user.getPassword()))) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    private User updateUserAuthorities(User givenUser, User user) {
    	givenUser.setRole(user.getRole());
    	return givenUser;
    }
}

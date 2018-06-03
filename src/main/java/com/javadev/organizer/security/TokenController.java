package com.javadev.organizer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.javadev.organizer.entities.User;

@RestController
public class TokenController {

	@Autowired
	private TokenService tokenService;
	
    @PostMapping("/auth/token")
    public String generate(@RequestBody final User user) {
        return tokenService.generateAuthToken(user);
    }
}

package com.javadev.organizer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.javadev.organizer.dto.DtoConverter;
import com.javadev.organizer.dto.UserDto;
import com.javadev.organizer.services.TokenService;

@RestController
public class TokenController {

	@Autowired
	private TokenService tokenService;
	
    @PostMapping("/auth/token")
    public String generate(@RequestBody final UserDto userDto) {
        return tokenService.generateAuthToken(DtoConverter.userFromDto(userDto));
    }
}

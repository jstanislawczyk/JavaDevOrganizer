package com.javadev.organizer.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.javadev.organizer.entities.Role;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class UserDto implements Serializable{
	
	@Getter
	private Long id;

	@Size(min = 2, max = 50, message = "First name must contain 2-50 characters")
	@Getter
	@Setter
	private String firstName;

	@Size(min = 2, max = 60, message = "Last name must contain 2-60 characters")
	@Getter
	@Setter
	private String lastName;

	@Email
	@Getter
	@Setter
	private String email;

	@Size(min = 6, max = 60, message = "Password must contain 6-60 characters")
	@Getter
	@Setter
	private String password;

	@Getter
	@Setter
	@Enumerated(EnumType.STRING)
	private Role role;

	@Getter
	@Setter
	private List<UserPresenceDto> userPresence;
}

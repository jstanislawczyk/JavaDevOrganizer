package com.javadev.organizer.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.javadev.organizer.entities.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable{
	
	@Getter
	private Long id;

	@Size(min = 2, max = 50, message = "{user.firstName.message}")
	@Getter
	@Setter
	private String firstName;

	@Size(min = 2, max = 60, message = "{user.lastName.message}")
	@Getter
	@Setter
	private String lastName;

	@Email(message="{user.email.message}")
	@Getter
	@Setter
	private String email;

	@Size(min = 6, max = 60, message = "{user.password.message}")
	@Getter
	@Setter
	private char[] password;

	@Getter
	@Setter
	@Enumerated(EnumType.STRING)
	private Role role;

	@Getter
	@Setter
	private List<UserPresenceDto> userPresence;
}

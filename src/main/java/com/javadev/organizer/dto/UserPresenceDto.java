package com.javadev.organizer.dto;

import java.io.Serializable;

import javax.persistence.GeneratedValue;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class UserPresenceDto implements Serializable{
	
	@GeneratedValue
	@Getter
	private Long id;

	@Getter
	@Setter
	private Long userId;

	@Getter
	@Setter
	private Long courseId;

	@Getter
	@Setter
	private Boolean present;
}

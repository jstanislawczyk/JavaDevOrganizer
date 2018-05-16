package com.javadev.organizer.dto;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class CourseDto implements Serializable {
	
	@Getter
	private Long id;
	
	@Size(min=5, max=150, message="Course name must contain 5-150 letters")
	@Getter
	@Setter
	private String name;
	
	@Size(min=5, max=300, message="Course description must contain 5-300 letters")
	@Getter
	@Setter
	private String description;
	
	@Getter
	@Setter
	private Date date;	
	
	@Getter
	@Setter
	private List<UserPresenceDto> coursePresence;
}

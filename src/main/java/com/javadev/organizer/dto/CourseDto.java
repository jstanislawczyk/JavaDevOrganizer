package com.javadev.organizer.dto;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto implements Serializable {
	
	@Getter
	private Long id;
	
	@Size(min=5, max=150, message="{course.name.message}")
	@Getter
	@Setter
	private String name;
	
	@Size(min=5, max=300, message="{course.description.message}")
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

package com.javadev.organizer.dto;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.javadev.organizer.entities.Course;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.entities.UserPresence;

@Component
public class DtoConverter {
	public static User userFromDto(UserDto userDto) {
		User user = new User.Builder()
				.id(userDto.getId())
				.firstName(userDto.getFirstName())
				.lastName(userDto.getLastName())
				.email(userDto.getEmail())
				.role(userDto.getRole())
				.password(userDto.getPassword())
				.build();
		
		return user;
	}
	
	public static UserDto dtoFromUser(User user) {
		UserDto userDto = new UserDto.UserDtoBuilder()
				.id(user.getId())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.role(user.getRole())
				.password(user.getPassword())
				.build();
		
		return userDto;
	}
	
	public static UserDto dtoFromUserWithPresence(User user) {
		UserDto userDto = dtoFromUser(user);
		userDto.setUserPresence(
				user.getUserPresence().stream().map(userPresence -> dtoFromUserPresence(userPresence)).collect(Collectors.toList()));
		
		return userDto;
	}
	
	public static CourseDto dtoFromCourse(Course course) {
		CourseDto courseDto = new CourseDto.CourseDtoBuilder()
				.id(course.getId())
				.name(course.getName())
				.description(course.getDescription())
				.date(course.getDate())
				.build();
		
		return courseDto;
	}
	
	public static UserPresenceDto dtoFromUserPresence(UserPresence userPresence) {
		UserPresenceDto userPresenceDto = new UserPresenceDto.UserPresenceDtoBuilder()
				.id(userPresence.getId())
				.present(userPresence.getPresent())
				.courseId(userPresence.getCourse().getId())
				.userId(userPresence.getCourse().getId())
				.build();
		
		return userPresenceDto;
	}
}

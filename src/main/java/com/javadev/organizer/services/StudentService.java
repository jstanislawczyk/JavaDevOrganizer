package com.javadev.organizer.services;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javadev.organizer.entities.Course;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.entities.UserPresence;
import com.javadev.organizer.exceptions.NotFoundException;
import com.javadev.organizer.exceptions.NotSavedException;
import com.javadev.organizer.repositories.CourseRepository;
import com.javadev.organizer.repositories.UserPresenceRepository;
import com.javadev.organizer.repositories.UserRepository;

@Service
public class StudentService {
	private CourseRepository courseRepository;
	private UserRepository userRepository;
	private UserPresenceRepository userPresenceRepository;

	@Autowired
	public StudentService(UserRepository userRepository, CourseRepository courseRepository,
			UserPresenceRepository userPresenceRepository) {
		this.userRepository = userRepository;
		this.courseRepository = courseRepository;
		this.userPresenceRepository = userPresenceRepository;
	}

	public void registerUserPresence(Long courseId, Long userId, boolean present) {

		User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User [id="+userId+"] not found"));
		Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotSavedException("Course [id="+courseId+"] not found"));

		if (isRegisteredAfterCourse(course) && isUserPresenceStatusUnique(user, course)) {
			saveStatus(user, course, present);
		} else {
			throw new NotSavedException("Presence not saved");
		}
	}

	public Map<Long, Boolean> getCoursesStatusByUserId(Long id) {

		User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User [id="+id+"] not found"));

		List<UserPresence> presences = userPresenceRepository.findByUser(user);
		Map<Long, Boolean> coursesStatus = new HashMap<>();
		
		presences.forEach(presence -> coursesStatus.put(presence.getCourse().getId(), presence.getPresent()));
		
		return coursesStatus;
	}

	private boolean isUserPresenceStatusUnique(User user, Course course) {
		if (userPresenceRepository.countByUserAndCourse(user, course)>0) {
			return false;
		} else {
			return true;
		}
	}

	private void saveStatus(User user, Course course, boolean present) {
		userPresenceRepository.save(UserPresence.builder().course(course).user(user).present(present).build());
	}

	private boolean isRegisteredAfterCourse(Course course) {
		Date today = new Date(System.currentTimeMillis());
		if (course.getDate().before(today)) {
			return true;
		} else {
			return false;
		}
	}
}

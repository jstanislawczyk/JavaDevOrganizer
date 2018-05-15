package com.javadev.organizer.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.javadev.organizer.entities.Course;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.entities.UserPresence;

public interface UserPresenceRepository extends CrudRepository<UserPresence, Long>{
	List<UserPresence> findByUser(User user);
	List<UserPresence> findByUserAndCourse(User user, Course course);
	Long countByUserAndCourse(User user, Course course);
}

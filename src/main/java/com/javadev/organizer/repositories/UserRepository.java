package com.javadev.organizer.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.javadev.organizer.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
	List<User> findByRole(String role);
	Optional<User> findByEmail(String email);
}

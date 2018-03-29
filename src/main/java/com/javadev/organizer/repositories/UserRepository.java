package com.javadev.organizer.repositories;

import org.springframework.data.repository.CrudRepository;

import com.javadev.organizer.entities.User;

public interface UserRepository extends CrudRepository<User, Long>{

}

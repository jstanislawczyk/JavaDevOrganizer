package com.javadev.organizer.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.javadev.organizer.entities.Course;

public interface CourseRepository extends CrudRepository<Course, Long> {
	
	@Query(value =   "SELECT id FROM course AS C "
					+ "INNER JOIN course_users AS CU "
					+ "ON C.id=CU.courses_id "
					+ "WHERE users_id = :userId"
			, nativeQuery = true)
	List<BigDecimal> findCoursesIdsByUserId(@Param("userId") Long userId);
}

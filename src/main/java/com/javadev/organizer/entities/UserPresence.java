package com.javadev.organizer.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "course_users")
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserPresence implements Serializable {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(targetEntity = User.class)
	private User user;

	@ManyToOne(targetEntity = Course.class)
	private Course course;

	private Boolean present;
}

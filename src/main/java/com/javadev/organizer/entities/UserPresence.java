package com.javadev.organizer.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "course_users")
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPresence implements Serializable {
	@Id
	@GeneratedValue
	@Getter
	private Long id;

	@ManyToOne(targetEntity = User.class)
	@Getter
	@Setter
	private User user;

	@ManyToOne(targetEntity = Course.class)
	@Getter
	@Setter
	private Course course;

	@Getter
	@Setter
	private Boolean present;
}

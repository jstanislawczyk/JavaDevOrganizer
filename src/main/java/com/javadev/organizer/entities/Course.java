package com.javadev.organizer.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Data
@Table(name = "course")
public class Course implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(min = 5, max = 150, message = "Course name must contain 5-150 letters")
	private String name;

	@Size(min = 5, max = 300, message = "Course description must contain 5-300 letters")
	private String description;

	private Date date;

	@OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
	@JsonBackReference
	private List<UserPresence> userPresence;

	public Course(final Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.description = builder.description;
		this.date = builder.date;
		this.userPresence = builder.userPresence;
	}

	public static class Builder {
		private Long id;
		private String name;
		private String description;
		private Date date;
		private List<UserPresence> userPresence;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Builder date(Date date) {
			this.date = date;
			return this;
		}

		public Builder users(List<UserPresence> userPresence) {
			this.userPresence = userPresence;
			return this;
		}

		public Course build() {
			return new Course(this);
		}
	}
}

package com.javadev.organizer.entities;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	private Long id;
	
	@Size(min=5, max=150, message="Course name must contain 5-150 letters")
	@Getter
	@Setter
	private String name;
	
	@Size(min=5, max=300, message="Course description must contain 5-300 letters")
	@Getter
	@Setter
	private String description;
	
	@Getter
	@Setter
	private Date date;	
	
	@ManyToMany(fetch = FetchType.LAZY)
	@Getter
	@Setter
	private List<User> users;
	
	public Course(final Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.description = builder.description;
		this.date = builder.date;
		this.users = builder.users;
	}
	
	public static class Builder{
		private Long id;	
		private String name;
		private String description;
		private Date date;
		private List<User> users;
		
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
		
		public Builder users(List<User> users) {
			this.users = users;
			return this;
		}
		
		public Course build() {
			return new Course(this);
		}
	}
}

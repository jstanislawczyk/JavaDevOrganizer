package com.javadev.organizer.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	private Long id;
	
	@Size(min=2, max=50, message="First name must contain 2-50 characters")
	@Getter
	@Setter
	private String firstName;
	
	@Size(min=2, max=60, message="Last name must contain 2-60 characters")
	@Getter
	@Setter
	private String lastName;
	
	@Email
	@Getter
	@Setter
	private String email;
	
	@Size(min=6, max=60, message="Password must contain 6-60 characters")
	@Getter
	@Setter
	private String password;
	
	@Getter
	@Setter
	private String role;
	
	@ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
	@JsonBackReference
	@Getter
	@Setter
	private List<Course> courses;
	
	public User(final Builder builder){
		this.id = builder.id;
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.email = builder.email;
		this.password = builder.password;
		this.role = builder.role;
		this.courses = builder.courses;
	}
	
	public static class Builder{
		private Long id;
		private String firstName;
		private String lastName;
		private String email;
		private String password;
		private String role;
		private List<Course> courses;
		
		public Builder id(Long id){
			this.id = id;
			return this;
		}
		
		public Builder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public Builder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public Builder email(String email) {
			this.email = email;
			return this;
		}
		
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		
		public Builder role(String role) {
			this.role = role;
			return this;
		}
		
		public Builder courses(List<Course> courses) {
			this.courses = courses;
			return this;
		}
		
		public User build() {
			return new User(this);
		}		
	}
}

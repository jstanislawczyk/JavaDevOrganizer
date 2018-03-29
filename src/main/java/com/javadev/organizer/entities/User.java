package com.javadev.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

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
	@Getter
	private Long id;
	
	@Size(min=2, max=50, message="First name must contain 2-50 characters")
	@Getter
	@Setter
	private String firstName;
	
	@Size(min=2, max=60, message="Last name must contain 2-50 characters")
	@Getter
	@Setter
	private String lastName;
	
	@Email
	@Getter
	@Setter
	private String email;
	
	public User(final Builder builder){
		this.id = builder.id;
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.email = builder.email;
	}
	
	public static class Builder{
		private Long id;
		private String firstName;
		private String lastName;
		private String email;
		
		public Builder id(final Long id){
			this.id = id;
			return this;
		}
		
		public Builder firstName(final String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public Builder lastName(final String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public Builder email(final String email) {
			this.email = email;
			return this;
		}
		
		public User build() {
			return new User(this);
		}		
	}
}

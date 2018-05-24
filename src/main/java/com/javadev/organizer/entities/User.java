package com.javadev.organizer.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Data
@Table(name = "users")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(min = 2, max = 50, message = "First name must contain 2-50 characters")
	private String firstName;

	@Size(min = 2, max = 60, message = "Last name must contain 2-60 characters")
	private String lastName;

	@Email
	private String email;

	@Size(min = 6, max = 60, message = "Password must contain 6-60 characters")
	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	@JsonBackReference
	private List<UserPresence> userPresence;

	public User(final Builder builder) {
		this.id = builder.id;
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.email = builder.email;
		this.password = builder.password;
		this.role = builder.role;
		this.userPresence = builder.userPresence;
	}

	public static class Builder {
		private Long id;
		private String firstName;
		private String lastName;
		private String email;
		private String password;
		private Role role;
		private List<UserPresence> userPresence;

		public Builder id(Long id) {
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

		public Builder role(Role role) {
			this.role = role;
			return this;
		}

		public Builder courses(List<UserPresence> userPresence) {
			this.userPresence = userPresence;
			return this;
		}

		public User build() {
			return new User(this);
		}
	}
}

package com.javadev.organizer.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http
			.csrf().disable();
		http
			.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/user/**").permitAll()
				.antMatchers("/lecturer/**").hasAnyAuthority("LECTURER","ADMIN")
				.antMatchers("/admin_control/**").hasAuthority("ADMIN");
		http
			.httpBasic()
			.authenticationEntryPoint(customAuthenticationEntryPoint);		
		
		http
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
			.usersByUsernameQuery("SELECT email, password, true FROM user WHERE email=?")
			.authoritiesByUsernameQuery("SELECT email, role FROM user WHERE email=?")
			.passwordEncoder(passwordEncoder());
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
}

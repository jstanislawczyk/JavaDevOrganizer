package com.javadev.organizer.security;

import org.springframework.stereotype.Component;

import com.javadev.organizer.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtGenerator {

    public String generate(User user) {

        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("password", user.getPassword());
        claims.put("role", user.getRole());
        
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, "JavaDev")
                .compact();
    }
}

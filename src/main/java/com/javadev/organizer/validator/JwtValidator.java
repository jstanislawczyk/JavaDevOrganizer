package com.javadev.organizer.validator;

import org.springframework.stereotype.Component;

import com.javadev.organizer.entities.Role;
import com.javadev.organizer.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtValidator {

    private String secret = "JavaDev";

    public User validate(String token) {

        User user = null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            user = new User();

            user.setEmail(body.getSubject());
            user.setRole(Role.valueOf( (String) body.get("role")));
            user.setPassword(( (String) body.get("password")).toCharArray());
            user.setId(Long.parseLong(String.valueOf(body.get("id"))));
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return user;
    }
}

package me.iqpizza.config.security.dto;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class AuthenticateUserStorage {

    private static final Map<String, AuthenticateUser> AUTHENTICATE_USER_MAP = new HashMap<>();

    public void addUser(AuthenticateUser authenticateUser) {
        AUTHENTICATE_USER_MAP.put(authenticateUser.getUsername(), authenticateUser);
    }

    public Optional<AuthenticateUser> findAuthenticateUser(String username) {
        return Optional.ofNullable(AUTHENTICATE_USER_MAP.get(username));
    }
}

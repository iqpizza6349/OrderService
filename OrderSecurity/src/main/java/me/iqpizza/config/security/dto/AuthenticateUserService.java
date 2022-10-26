package me.iqpizza.config.security.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticateUserService implements UserDetailsService {

    private final AuthenticateUserStorage authenticateUserStorage;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authenticateUserStorage.findAuthenticateUser(username)
                .orElseThrow();
    }
}

package com.jselva.nisum.evaluationtest.services;

import com.jselva.nisum.evaluationtest.configuration.security.JwtProvider;
import com.jselva.nisum.evaluationtest.data.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsSecurityServiceImpl implements UserDetailsSecurityService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public UserDetailsSecurityServiceImpl(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Optional<UserDetails> loadUserByJwtToken(String token) {

        if (jwtProvider.validateToken(token)) {
            return Optional.of(
                    org.springframework.security.core.userdetails.User.withUsername(jwtProvider.getUsername(token))
                            .authorities(jwtProvider.getRoles(token))
                            .password("")
                            .accountExpired(false)
                            .accountLocked(false)
                            .credentialsExpired(false)
                            .disabled(false)
                            .build());
        }
        return Optional.empty();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = this.userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("Usuario o contraseña invalidas"));

        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles())
                .credentialsExpired(false)
                .accountLocked(false)
                .accountExpired(false)
                .disabled(false)
                .build();
    }
}

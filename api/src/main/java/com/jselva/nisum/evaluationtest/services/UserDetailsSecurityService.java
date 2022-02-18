package com.jselva.nisum.evaluationtest.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserDetailsSecurityService extends UserDetailsService {
    Optional<UserDetails> loadUserByJwtToken(String jwtToken);
}

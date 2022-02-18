package com.jselva.nisum.evaluationtest.configuration.security;

import com.jselva.nisum.evaluationtest.services.UserDetailsSecurityService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String PREFIX_BEARER = "Bearer";
    private final UserDetailsSecurityService userDetailsSecurityService;

    public JwtTokenFilter(UserDetailsSecurityService userDetailsSecurityService) {
        this.userDetailsSecurityService = userDetailsSecurityService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authorizationHeader = ((HttpServletRequest) request).getHeader(AUTHORIZATION);

        if (Objects.nonNull(authorizationHeader)) {

            getBearerToken(authorizationHeader)
                    .flatMap(this.userDetailsSecurityService::loadUserByJwtToken)
                    .ifPresent(userDetails -> {
                        SecurityContextHolder.getContext().setAuthentication(
                                new PreAuthenticatedAuthenticationToken(
                                        userDetails, "", userDetails.getAuthorities())
                        );
                    });
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return true;
    }

    private Optional<String> getBearerToken(String authorizationHeader) {
        if (Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith(PREFIX_BEARER)) {
            return Optional.of(authorizationHeader.replace(PREFIX_BEARER, "").trim());
        }
        return Optional.empty();
    }
}

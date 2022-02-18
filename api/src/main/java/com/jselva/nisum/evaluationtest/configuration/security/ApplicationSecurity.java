package com.jselva.nisum.evaluationtest.configuration.security;

import com.jselva.nisum.evaluationtest.services.UserDetailsSecurityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    private final String[] AUTHORIZED_URLS = new String[]{
            "/",
            "/h2-console/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/actuator/health/**"
    };

    private final UserDetailsSecurityService userDetailsSecurityService;

    public ApplicationSecurity(UserDetailsSecurityService userDetailsSecurityService) {
        this.userDetailsSecurityService = userDetailsSecurityService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsSecurityService)
                .passwordEncoder(this.passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors()
                .and()
                .csrf()
                .disable();

        http.headers()
                .frameOptions()
                .disable();

        http.authorizeRequests()
                .antMatchers(AUTHORIZED_URLS)
                .permitAll()
                .antMatchers("/user/login/**")
                .permitAll()
                .anyRequest()
                .authenticated();

        http.addFilterBefore(new JwtTokenFilter(this.userDetailsSecurityService),
                UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}

package com.jselva.nisum.evaluationtest.configuration;

import com.jselva.nisum.evaluationtest.configuration.security.SecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class DataConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return new SecurityAuditorAware();
    }
}

package com.git.stavrosdim.sw.simplewebsite.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

    private final FrontendConfig frontendConfig;

    public CorsConfiguration(FrontendConfig frontendConfig) {
        this.frontendConfig = frontendConfig;
    }

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(frontendConfig.getFrontendUrl())
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}

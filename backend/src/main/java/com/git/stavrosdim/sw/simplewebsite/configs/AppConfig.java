package com.git.stavrosdim.sw.simplewebsite.configs;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FrontendConfig.class)
public class AppConfig {
}

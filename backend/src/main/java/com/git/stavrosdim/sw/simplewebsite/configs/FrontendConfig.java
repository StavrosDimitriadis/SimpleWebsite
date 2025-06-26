package com.git.stavrosdim.sw.simplewebsite.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "custom-properties.urls")
@Getter
@Setter
public class FrontendConfig {

    String frontendUrl;
    String displayUsersUrl;
    String registerUserUrl;
}

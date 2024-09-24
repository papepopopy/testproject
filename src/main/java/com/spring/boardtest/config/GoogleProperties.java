package com.spring.boardtest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "google")
public class GoogleProperties {
    private String clientId;
    private String clientSecret;
}
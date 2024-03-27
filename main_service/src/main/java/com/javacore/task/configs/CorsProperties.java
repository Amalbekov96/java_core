package com.javacore.task.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "web.cors")
public class CorsProperties {
    private List<String> allowedOrigins;
    private List<String> allowedMethods;
    private Long maxAge;
    private List<String> allowedHeaders;
    private List<String> exposedHeaders;
}

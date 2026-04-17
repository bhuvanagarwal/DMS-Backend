package com.dms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "app.cors")
@Data
public class CorsProperties {

    private List<String> allowedOrigins = List.of("http://localhost:5173"); // default fallback
}
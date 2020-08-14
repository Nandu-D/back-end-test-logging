package com.example.backendtestlogging.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "database")
@Getter
@Setter
public class Database {
    private String url;
}

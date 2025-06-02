package com.demo.openapi.config.ws;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "websocket")
@Setter
@Getter
public class WebSocketConfig {
    private String protocol;
    private String path;
    private String host;
    private int port;
    private int connectionTimeout;
}

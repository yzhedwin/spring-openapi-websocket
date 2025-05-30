package com.demo.openapi.config.ws;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.websocket.ClientWebSocketContainer;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "websocket")
@Setter
public class WebSocketConfig {
    private String protocol;
    private String path;
    private String host;
    private int port;

    @Bean
    public ClientWebSocketContainer clientWebSocketContainer() {
        return new ClientWebSocketContainer(new StandardWebSocketClient(), protocol + "://" + host + ":" + port + path);
    }
}

package com.demo.openapi.config.ws;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.websocket.ClientWebSocketContainer;
import org.springframework.web.socket.client.WebSocketClient;
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
        try {
            WebSocketClient webSocketClient = new StandardWebSocketClient();
            return new ClientWebSocketContainer(webSocketClient,
                    protocol + "://" + host + ":" + port + path);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create WebSocket client container", e);
        }
    }
}

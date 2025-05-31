package com.demo.openapi.config.ws;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.websocket.ClientWebSocketContainer;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

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

    @Bean
    public ClientWebSocketContainer clientWebSocketContainer() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        URI uri = URI.create(protocol + "://" + host + ":" + port + path);
        return new ClientWebSocketContainer(webSocketClient, uri);
    }
}

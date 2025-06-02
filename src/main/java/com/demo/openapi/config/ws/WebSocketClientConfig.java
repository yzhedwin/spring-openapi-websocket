package com.demo.openapi.config.ws;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.websocket.ClientWebSocketContainer;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
public class WebSocketClientConfig {
    private final WebSocketConfig webSocketConfig;

    public WebSocketClientConfig(WebSocketConfig webSocketConfig) {
        this.webSocketConfig = webSocketConfig;
    }

    @Bean
    @Profile({ "client", "test" })
    public ClientWebSocketContainer clientWebSocketContainer() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        URI uri = URI.create(webSocketConfig.getProtocol() + "://" + webSocketConfig.getHost() + ":"
                + webSocketConfig.getPort() + webSocketConfig.getPath());
        return new ClientWebSocketContainer(webSocketClient, uri);
    }
}

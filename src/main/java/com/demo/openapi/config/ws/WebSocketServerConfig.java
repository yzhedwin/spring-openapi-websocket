package com.demo.openapi.config.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.demo.openapi.handler.ws.WebSocketServerMessageHandler;

@Configuration
@EnableWebSocket
@Profile("server")
public class WebSocketServerConfig implements WebSocketConfigurer {
    private final WebSocketServerMessageHandler webSocketServerMessageHandler;

    private final WebSocketConfig webSocketConfig;

    public WebSocketServerConfig(WebSocketConfig webSocketConfig,
            WebSocketServerMessageHandler webSocketServerMessageHandler) {
        this.webSocketServerMessageHandler = webSocketServerMessageHandler;
        this.webSocketConfig = webSocketConfig;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketServerMessageHandler, webSocketConfig.getPath()).setAllowedOrigins("*");
    }
}
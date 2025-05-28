package com.demo.openapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.demo.openapi.handler.WebSocketHandlerImpl;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(webSocketHandler(), "/websocket")
        .setAllowedOrigins("*"); // Allow all origins for simplicity; adjust as needed
  }

  @Bean
  public WebSocketHandlerImpl webSocketHandler() {
    return new WebSocketHandlerImpl();
  }

}

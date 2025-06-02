package com.demo.openapi.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.websocket.ClientWebSocketContainer;
import org.springframework.integration.websocket.ServerWebSocketContainer;
import org.springframework.integration.websocket.inbound.WebSocketInboundChannelAdapter;
import org.springframework.integration.websocket.outbound.WebSocketOutboundMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.socket.TextMessage;

import com.demo.openapi.handler.web.WebAPIHandler;
import com.demo.openapi.handler.ws.WebSocketServerHandler;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class IntegrationConfig {
  @Bean
  @Profile({ "client", "test" })
  public MessageChannel receivedFromWebSocket() {
    return new DirectChannel();
  }

  @Bean
  @Profile({ "client", "test" })
  public WebSocketInboundChannelAdapter webSocketInboundChannelAdapter(
      ClientWebSocketContainer clientWebSocketContainer) {
    WebSocketInboundChannelAdapter adapter = new WebSocketInboundChannelAdapter(clientWebSocketContainer);
    adapter.setOutputChannel(receivedFromWebSocket());
    adapter.setAutoStartup(false);
    return adapter;
  }

  @Bean
  @Profile({ "client", "test" })
  public IntegrationFlow webSocketInboundFlow() {
    return IntegrationFlow
        .from(receivedFromWebSocket())
        .handle("webSocketMessageHandler", "handle")
        .get();
  }

  // // Outbound channel (to send messages)
  @Bean
  @Profile("server")
  public MessageChannel transmitToWebSocket() {
    return new DirectChannel();
  }

  @Bean
  @Profile("server")
  public IntegrationFlow webSocketOutboundFlow(WebSocketServerHandler webSocketServerHandler) {
    return IntegrationFlow
        .from(transmitToWebSocket()) //todo cleanup
        .handle(Message.class, (msg, headers) -> {
          String payload = msg.getPayload().toString();
          TextMessage message = new TextMessage(payload);
          webSocketServerHandler.getSessions().forEach(session -> {
                try {
                  if (session.isOpen()) {
                    session.sendMessage(message);
                  }
                  } catch (IOException e) {
                    log.error("Error sending message to WebSocket session: " + session.getId(), e);
                  }
              });
          return null; // No reply
        })
        .get();
  }

  // API request channel (to send requests to the REST API)
  @Bean
  @Profile({ "client", "test" })
  public MessageChannel apiRequestChannelScheduleUpdated() {
    return new DirectChannel();
  }

  @Bean
  @Profile({ "client", "test" })
  public IntegrationFlow restAPIIntegrationFlow(WebAPIHandler webClientHandler,
      @Qualifier("apiRequestChannelScheduleUpdated") MessageChannel apiRequestChannelScheduleUpdated) {
    return IntegrationFlow.from(apiRequestChannelScheduleUpdated)
        .split()
        // .transform()
        .handle(webClientHandler, "apiRequestHandlerForScheduleUpdate")
        .get();
  }
}

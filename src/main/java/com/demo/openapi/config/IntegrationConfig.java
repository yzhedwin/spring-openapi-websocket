package com.demo.openapi.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.websocket.ClientWebSocketContainer;
import org.springframework.integration.websocket.inbound.WebSocketInboundChannelAdapter;
import org.springframework.messaging.MessageChannel;

import com.demo.openapi.handler.web.WebAPIHandler;
import com.demo.openapi.handler.ws.WebSocketClientMessageHandler;
import com.demo.openapi.handler.ws.WebSocketServerMessageHandler;

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
  public IntegrationFlow webSocketInboundFlow(WebSocketClientMessageHandler webSocketClientMessageHandler) {
    return IntegrationFlow
        .from(receivedFromWebSocket())
        .handle(webSocketClientMessageHandler, "handle")
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
  public IntegrationFlow webSocketOutboundFlow(WebSocketServerMessageHandler webSocketServerMessageHandler) {
    return IntegrationFlow
        .from(transmitToWebSocket()) // todo cleanup
        .handle(webSocketServerMessageHandler, "sendMessageToAll")
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

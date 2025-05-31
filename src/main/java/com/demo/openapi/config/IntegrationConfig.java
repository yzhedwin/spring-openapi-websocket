package com.demo.openapi.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.websocket.ClientWebSocketContainer;
import org.springframework.integration.websocket.inbound.WebSocketInboundChannelAdapter;
import org.springframework.messaging.MessageChannel;

import com.demo.openapi.handler.web.WebAPIHandler;

@Configuration
public class IntegrationConfig {

  @Bean
  public MessageChannel receivedFromWebSocket() {
    return new DirectChannel();
  }

  @Bean
  public WebSocketInboundChannelAdapter webSocketInboundChannelAdapter(
      ClientWebSocketContainer clientWebSocketContainer) {
    WebSocketInboundChannelAdapter adapter = new WebSocketInboundChannelAdapter(clientWebSocketContainer);
    adapter.setOutputChannel(receivedFromWebSocket());
    adapter.setAutoStartup(false);
    return adapter;
  }

  @Bean
  public IntegrationFlow webSocketInboundFlow() {
    return IntegrationFlow
        .from(receivedFromWebSocket())
        .handle("webSocketMessageHandler", "handle")
        .get();
  }

  // // Outbound channel (to send messages)
  // @Bean
  // public MessageChannel transmitToWebSocket() {
  // return new DirectChannel();
  // }

  // @Bean
  // public StandardIntegrationFlow webSocketOutboundFlow() {
  // return IntegrationFlow
  // .from(transmitToWebSocket())
  // .handle(webSocketOutboundMessageHandler())
  // .get();
  // }

  // @Bean
  // public WebSocketOutboundMessageHandler webSocketOutboundMessageHandler() {
  // WebSocketOutboundMessageHandler handler = new
  // WebSocketOutboundMessageHandler(clientWebSocketContainer());
  // return handler;
  // }

  // API request channel (to send requests to the REST API)
  @Bean
  public MessageChannel apiRequestChannelScheduleUpdated() {
    return new DirectChannel();
  }

  @Bean
  public IntegrationFlow restAPIIntegrationFlow(WebAPIHandler webClientHandler,
      @Qualifier("apiRequestChannelScheduleUpdated") MessageChannel apiRequestChannelScheduleUpdated) {
    return IntegrationFlow.from(apiRequestChannelScheduleUpdated)
        .split()
        // .transform()
        .handle(webClientHandler, "apiRequestHandlerForScheduleUpdate")
        .get();
  }
}

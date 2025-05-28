package com.demo.openapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;

import com.demo.openapi.handler.ExternalMessageHandler;
import com.demo.openapi.handler.WebClientHandler;

@Configuration
public class IntegrationConfig {
  @Bean
  public MessageChannel webSocketChannel() {
    return new DirectChannel();
  }

  @Bean
  public MessageChannel apiRequestChannel() {
    return new DirectChannel();
  }

  @Bean
  public IntegrationFlow webSocketIntegrationFlow(ExternalMessageHandler externalMessageHandler) {
    return IntegrationFlow.from(webSocketChannel())
        // .transform(new StringToUpperCaseTransformer())
        .split()
        .handle(externalMessageHandler, "webSocketMessageHandler")
        .get();
  }

  @Bean
  public IntegrationFlow restAPIIntegrationFlow(WebClientHandler webClientHandler) {
    return IntegrationFlow.from(apiRequestChannel())
        .split()
        // .transform()
        .handle(webClientHandler, "webSocketMessageHandler")
        .get();
  }
}

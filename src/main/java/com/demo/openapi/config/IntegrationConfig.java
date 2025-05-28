package com.demo.openapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;

import com.demo.openapi.handler.ExternalMessageHandler;

@Configuration
public class IntegrationConfig {
  @Bean
  public MessageChannel webSocketChannel() {
    return new DirectChannel();
  }

  @Bean
  public IntegrationFlow webSocketIntegrationFlow(ExternalMessageHandler externalMessageHandler) {
    return IntegrationFlow.from(webSocketChannel())
        // .transform(new StringToUpperCaseTransformer())
        .handle(externalMessageHandler, "webSocketMessageHandler")
        .get();
  }

}

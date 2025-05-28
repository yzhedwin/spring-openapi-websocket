package com.demo.openapi.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.webflux.outbound.WebFluxRequestExecutingMessageHandler;
import org.springframework.web.reactive.function.client.WebClient;

import com.demo.openapi.config.WebClientConfig;

public class WebClientHandler {
  WebClientConfig config;

  public WebClientHandler(WebClientConfig config) {
    this.config = config;
  }

  @ServiceActivator(inputChannel = "reactiveHttpOutRequest")
  @Bean
  public WebFluxRequestExecutingMessageHandler reactiveOutbound(WebClient client) {
    WebFluxRequestExecutingMessageHandler handler = new WebFluxRequestExecutingMessageHandler(
        config.getBaseUrl() + "/foo", client);
    handler.setHttpMethod(HttpMethod.POST);
    handler.setExpectedResponseType(String.class);
    return handler;
  }
}

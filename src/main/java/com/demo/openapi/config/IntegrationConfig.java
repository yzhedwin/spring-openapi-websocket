package com.demo.openapi.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.integration.websocket.ClientWebSocketContainer;
import org.springframework.integration.websocket.inbound.WebSocketInboundChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

import com.demo.common.model.SimpleAssetMission;
import com.demo.openapi.handler.ScheduledTaskTransformer;
import com.demo.openapi.handler.SimpleAssetMissionTransformer;
import com.demo.openapi.handler.web.WebAPIHandler;
import com.demo.openapi.handler.ws.WebSocketClientMessageHandler;
import com.demo.openapi.handler.ws.WebSocketServerMessageHandler;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class IntegrationConfig {
  @Bean
  @Profile({ "client", "test" })
  MessageChannel receivedFromWebSocket() {
    return new DirectChannel();
  }

  @Bean
  @Profile({ "client", "test" })
  WebSocketInboundChannelAdapter webSocketInboundChannelAdapter(
      ClientWebSocketContainer clientWebSocketContainer) {
    WebSocketInboundChannelAdapter adapter = new WebSocketInboundChannelAdapter(clientWebSocketContainer);
    adapter.setOutputChannel(receivedFromWebSocket());
    adapter.setAutoStartup(false);
    return adapter;
  }

  @Bean
  @Profile({ "client", "test" })
  IntegrationFlow webSocketInboundFlow(WebSocketClientMessageHandler webSocketClientMessageHandler,
      @Qualifier("simpleAssetMissionTransformer") SimpleAssetMissionTransformer transformer) {
    return IntegrationFlow
        .from(receivedFromWebSocket())
        .transform(new JsonToObjectTransformer(SimpleAssetMission.class))
        .transform(transformer)
        .transform(new ObjectToJsonTransformer())
        .handle(webSocketClientMessageHandler, "handle")
        .get();
  }

  // // Outbound channel (to send messages)
  @Bean
  @Profile("server")
  MessageChannel transmitToWebSocket() {
    return new DirectChannel();
  }

  @Bean
  @Profile("server")
  IntegrationFlow webSocketOutboundFlow(WebSocketServerMessageHandler webSocketServerMessageHandler,
      @Qualifier("scheduledTaskTransformer") ScheduledTaskTransformer transformer) {
    return IntegrationFlow
        .from(transmitToWebSocket()) // todo cleanup
        .transform(transformer)
        .handle(webSocketServerMessageHandler, "sendMessageToAll")
        .get();
  }

  // API request channel (to send requests to the REST API)
  @Bean
  MessageChannel requestChannel() {
    return new PublishSubscribeChannel();
  }

  @Bean
  PollableChannel replyChannel() {
    return new QueueChannel();
  }

  @Bean
  @Profile({ "client", "test" })
  IntegrationFlow patchFlow(WebAPIHandler webClientHandler,
      @Qualifier("requestChannel") MessageChannel requestChannel) {
    return IntegrationFlow.from(requestChannel)
        .split()
        .filter(Message.class, message -> {
          HttpMethod method = (HttpMethod) message.getHeaders().get("http_method",
              HttpMethod.class);
          return HttpMethod.PATCH.equals(method);
        })
        .handle(webClientHandler, "apiChannelSchedulePatch")
        .get();
  }

  @Bean
  @Profile({ "client", "test" })
  IntegrationFlow putFlow(WebAPIHandler webClientHandler,
      @Qualifier("requestChannel") MessageChannel requestChannel,
      @Qualifier("scheduledTaskTransformer") ScheduledTaskTransformer transformer) {
    return IntegrationFlow.from(requestChannel)
        .split()
        .filter(Message.class, message -> {
          HttpMethod method = (HttpMethod) message.getHeaders().get("http_method",
              HttpMethod.class);
          return HttpMethod.PUT.equals(method);

        })
        .transform(transformer)
        .handle(webClientHandler, "apiChannelSchedulePut")
        .get();
  }

  @Bean
  @Profile({ "client", "test" })
  IntegrationFlow deleteFlow(WebAPIHandler webClientHandler,
      @Qualifier("requestChannel") MessageChannel requestChannel,
      @Qualifier("scheduledTaskTransformer") ScheduledTaskTransformer transformer) {
    return IntegrationFlow.from(requestChannel)
        .split()
        .filter(Message.class, message -> {
          HttpMethod method = (HttpMethod) message.getHeaders().get("http_method",
              HttpMethod.class);
          return HttpMethod.DELETE.equals(method);

        })
        .transform(transformer)
        .handle(webClientHandler, "apiChannelScheduleDelete")
        .get();
  }

  @Bean
  IntegrationFlow getFlow(WebAPIHandler webClientHandler,
      @Qualifier("requestChannel") MessageChannel requestChannel) {
    return IntegrationFlow.from(requestChannel)
        .split()
        .filter(Message.class, message -> {
          HttpMethod method = (HttpMethod) message.getHeaders().get("http_method",
              HttpMethod.class);
          return HttpMethod.GET.equals(method);
        })
        .handle(webClientHandler, "apiChannelScheduleGet")
        .channel(replyChannel())
        .log()
        .get();
  }

  @Bean
  ScheduledTaskTransformer scheduledTaskTransformer() {
    return new ScheduledTaskTransformer();
  }

  @Bean
  SimpleAssetMissionTransformer simpleAssetMissionTransformer() {
    return new SimpleAssetMissionTransformer();
  }
}

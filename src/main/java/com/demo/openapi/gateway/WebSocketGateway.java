package com.demo.openapi.gateway;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.demo.common.model.SimpleAssetMission;
import com.demo.openapi.model.EHttp;

import lombok.extern.slf4j.Slf4j;

@Profile("server")
@Slf4j
@Component
public class WebSocketGateway implements GatewayInterface {
    private final MessageChannel transmitToWebSocket;
    private final MessageChannel requestChannel;

    private final PollableChannel replyChannel;

    public WebSocketGateway(@Qualifier("transmitToWebSocket") MessageChannel transmitToWebSocket,
            @Qualifier("requestChannel") MessageChannel requestChannel,
            @Qualifier("replyChannel") PollableChannel replyChannel) {
        this.transmitToWebSocket = transmitToWebSocket;
        this.requestChannel = requestChannel;
        this.replyChannel = replyChannel;
    }

    @Override
    public void send(Object event) {
        // Send via WebSocket
        System.out.println("Sending event to WebSocket");
        transmitToWebSocket.send(MessageBuilder.withPayload(event).build());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SimpleAssetMission> receive() {
        requestChannel.send(MessageBuilder.withPayload(new Object()).setHeader("http_method", HttpMethod.GET)
                .build());

        Message<?> reply = replyChannel.receive(5000);
        if (reply != null && reply.getPayload() instanceof List) {
            return (List<SimpleAssetMission>) reply.getPayload();
        }
        return List.of();
    }

    @Override
    public void send(Object event, EHttp type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'send'");
    }

}
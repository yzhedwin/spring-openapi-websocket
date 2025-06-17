package com.demo.openapi.gateway;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.stereotype.Component;

import com.demo.common.model.SimpleAssetMission;
import com.demo.openapi.model.EHttp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile({ "client", "test" })
@Component
public class RestAPIGateway implements GatewayInterface {

    private final MessageChannel requestChannel;

    private final PollableChannel replyChannel;

    public RestAPIGateway(
            @Qualifier("requestChannel") MessageChannel requestChannel,
            @Qualifier("replyChannel") PollableChannel replyChannel) {
        this.requestChannel = requestChannel;
        this.replyChannel = replyChannel;
    }

    @Override
    public void send(Object event, EHttp type) {
        log.info("Sending event to API");
        switch (type) {
            case DELETE:
                requestChannel
                        .send(MessageBuilder.withPayload(event)
                                .setHeader("http_method", HttpMethod.DELETE)
                                .build());
                break;
            case PUT:
                requestChannel
                        .send(MessageBuilder.withPayload(event)
                                .setHeader("http_method", HttpMethod.PUT)
                                .build());
                break;
            case PATCH:
                requestChannel
                        .send(MessageBuilder.withPayload(event)
                                .setHeader("http_method", HttpMethod.PATCH)
                                .build());
                break;
            default:
                break;
        }
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
    public void send(Object event) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'send'");
    }

}

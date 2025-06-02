package com.demo.openapi.config.ws;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.websocket.inbound.WebSocketInboundChannelAdapter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Profile({ "client", "test" })
@Component
@Slf4j
public class WebSocketLifecycle implements SmartLifecycle {

    private final WebSocketInboundChannelAdapter adapter;
    private final WebSocketConfig webSocketConfig;
    private boolean running = false;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public WebSocketLifecycle(WebSocketInboundChannelAdapter adapter, WebSocketConfig webSocketConfig) {
        this.webSocketConfig = webSocketConfig;
        this.adapter = adapter;
    }

    @Override
    public void start() {
        scheduler.execute(this::attemptStart);
    }

    private void attemptStart() {
        if (!running) {
            try {
                adapter.start(); // manually start the adapter
                running = true;
                log.info("WebSocket connection established.");
            } catch (Exception e) {
                log.error("WebSocket connection failed. Retrying in 5 seconds...");
                scheduler.schedule(this::attemptStart, webSocketConfig.getConnectionTimeout(),
                        TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public void stop() {
        adapter.stop();
        running = false;
        scheduler.shutdownNow();
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
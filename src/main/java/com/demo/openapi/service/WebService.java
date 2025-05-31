package com.demo.openapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class WebService {

	private final WebClient webClient;

	public WebService(WebClient webClient) {
		this.webClient = webClient;
	}

	public Mono<String> postRequest(Object message, String endpoint) {
		return this.webClient.post().uri(endpoint).bodyValue(message).retrieve()
				.bodyToMono(String.class).onErrorResume(fallback -> {
					System.err.println("Error during request: " + fallback.getMessage());
					return Mono.just("Fallback response due to error: " + fallback.getMessage());
				});

	}
}
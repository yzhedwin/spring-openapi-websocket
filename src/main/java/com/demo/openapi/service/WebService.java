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

	public Mono<String> someRestCall(String name) {
		return this.webClient.get().uri("/{name}/details", name).retrieve().bodyToMono(String.class);
	}

}
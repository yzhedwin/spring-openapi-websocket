package com.test.openapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class TWebService {

	private final WebClient webClient;

	public TWebService(WebClient webClient) {
		this.webClient = webClient;
	}

public Mono<String> someRestCall(String name) {
    return this.webClient.get().uri("/{name}/details", name).retrieve().bodyToMono(String.class);
	}

}
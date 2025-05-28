package com.demo.openapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.demo.openapi.model.SchedulerModel;

import reactor.core.publisher.Mono;

@Service
public class WebService {

	private final WebClient webClient;

	public WebService(WebClient webClient) {
		this.webClient = webClient;
	}

	public Mono<String> postSchedulerChange(SchedulerModel schedulerModel) {
		return this.webClient.post().uri("/scheduler").bodyValue(schedulerModel).retrieve().bodyToMono(String.class);
	}

}
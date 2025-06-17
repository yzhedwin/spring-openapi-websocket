package com.demo.openapi.service;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.demo.common.model.SimpleAssetMission;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class WebService {

	private final WebClient webClient;

	public WebService(WebClient webClient) {
		this.webClient = webClient;
	}

	public Mono<String> postSimpleAssetMission(SimpleAssetMission message, String endpoint) {
		return this.webClient.post().uri(endpoint).bodyValue(message).exchangeToMono(response -> {
			log.info("[PUT STATUS]: " + response.statusCode());
			return response.bodyToMono(String.class);
		}).doOnNext(body -> log.info("Body " + body));

	}

	public Mono<String> putSimpleAssetMission(SimpleAssetMission message, String endpoint) {
		log.info("Sending PUT request to {} ", endpoint, message.toString());
		return this.webClient.put().uri(endpoint).bodyValue(message).exchangeToMono(response -> {
			log.info("[PUT STATUS]: " + response.statusCode());
			return response.bodyToMono(String.class);
		}).doOnNext(body -> log.info("Body " + body));

	}

	public Mono<List<SimpleAssetMission>> getAllSimpleAssetMission(String endpoint) {
		return this.webClient.get().uri(endpoint).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError,
						response -> Mono.error(new RuntimeException("Client Error")))
				.onStatus(HttpStatusCode::is5xxServerError,
						response -> Mono.error(new RuntimeException("Server Error")))
				.bodyToFlux(SimpleAssetMission.class)
				.onErrorResume(fallback -> {
					System.err.println("Error during request: " + fallback.getMessage());
					return Mono.empty();
				}).collectList();

	}

	public Mono<String> patchSimpleAssetMission(String endpoint, String resource, long id, String data) {
		return this.webClient.patch().uri(endpoint + "/{id}" + resource, id).bodyValue(data)
				.exchangeToMono(response -> {
					log.info("[PATCH STATUS]: " + response.statusCode());
					return response.bodyToMono(String.class);
				}).doOnNext(body -> log.info("Body " + body));

	}

	public Mono<String> patchSimpleAssetMission(String endpoint, String resource, long id, long data) {
		return this.webClient.patch().uri(endpoint + "/{id}" + resource, id).bodyValue(data)
				.exchangeToMono(response -> {
					log.info("[PATCH STATUS]: " + response.statusCode());
					return response.bodyToMono(String.class);
				}).doOnNext(body -> log.info("Body " + body));

	}

	public Mono<String> deleteSimpleAssetMission(int id, String endpoint) {
		return this.webClient.delete().uri(endpoint + "/{id}", id).exchangeToMono(response -> {
			log.info("[DELETE STATUS]: " + response.statusCode());
			return response.bodyToMono(String.class);
		}).doOnNext(body -> log.info("Body " + body));
	}
}
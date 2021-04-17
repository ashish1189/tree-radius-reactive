package com.holidu.interview.assignment.controller;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.holidu.interview.assignment.exception.TreesErrorAttributes;
import com.holidu.interview.assignment.service.TreesService;

import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(TreesController.class)
class TreeControllerTest {

	@Autowired
	WebTestClient webTestClient;
	
	@MockBean
	private TreesService treesService; 
	
	@Test
	void getTreesResponseOk() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("honeylocust", 2);
		Mono<Map<String, Integer>> mono = Mono.just(map);
		
		when(treesService.getTrees(1002420.358, 199244.2531, 10)).thenReturn(mono);
		
		webTestClient
			.get()
			.uri(uriBuilder -> uriBuilder
					.path("/api/v1/all-trees")
					.queryParam("x", 1002420.358)
					.queryParam("y", 199244.2531)
					.queryParam("radius", 10)
					.build())
			.accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(HashMap.class)
            .value(map1 -> map.get("honeylocust"), equalTo(2));
	}
	
	@Test
	void invalidRadiusTest() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("honeylocust", 2);
		Mono<Map<String, Integer>> mono = Mono.just(map);
		
		when(treesService.getTrees(1002420.358, 199244.2531, -10)).thenReturn(mono);
		
		webTestClient
			.get()
			.uri(uriBuilder -> uriBuilder
					.path("/api/v1/all-trees")
					.queryParam("x", 1002420.358)
					.queryParam("y", 199244.2531)
					.queryParam("radius", -10)
					.build())
			.accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(matcher -> matcher.contains("Radius should be non zero positive"));
	}
	
	@Test
	void invalidXTest() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("honeylocust", 2);
		Mono<Map<String, Integer>> mono = Mono.just(map);
		
		when(treesService.getTrees(1002420.358, 199244.2531, -10)).thenReturn(mono);
		
		webTestClient
			.get()
			.uri(uriBuilder -> uriBuilder
					.path("/api/v1/all-trees")
					.queryParam("x", "a")
					.queryParam("y", 199244.2531)
					.queryParam("radius", -10)
					.build())
			.accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(matcher -> matcher.contains("Type mismatch."));
	}
	
	@Test
	void invalidYTest() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("honeylocust", 2);
		Mono<Map<String, Integer>> mono = Mono.just(map);
		
		when(treesService.getTrees(1002420.358, 199244.2531, -10)).thenReturn(mono);
		
		webTestClient
			.get()
			.uri(uriBuilder -> uriBuilder
					.path("/api/v1/all-trees")
					.queryParam("x", 1002420.358)
					.queryParam("y", "s")
					.queryParam("radius", -10)
					.build())
			.accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(matcher -> matcher.contains("Type mismatch."));
	}
	
	@org.springframework.boot.test.context.TestConfiguration
	static class TestConfiguration {
		@Bean
		public TreesErrorAttributes getTreesErrorAttributes() {
			return new TreesErrorAttributes();
		}
	}
}

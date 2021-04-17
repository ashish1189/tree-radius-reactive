package com.holidu.interview.assignment.service;

import static org.hamcrest.CoreMatchers.equalTo;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.holidu.interview.assignment.utils.Utilities;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
class TreeServiceImplTest {
	
	@Mock
	Map<String, Integer> map;

	@Test
	void getTreesTest(@Autowired WebTestClient webTestClient) {
		
		map = new HashMap<String, Integer>();
		
		map.put("American elm", 3);
		map.put("common hackberry", 1);
		map.put("Japanese zelkova", 13);
		map.put("sweetgum", 1);
		map.put("tree of heaven", 1);
		map.put("English oak", 4);
		map.put("honeylocust", 19);
		map.put("Callery pear", 2);
		map.put("ginkgo", 3);
		map.put("pin oak", 4);
		map.put("hardy rubber tree", 1);
		map.put("Default", 1);
		
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
        .value(map1 -> map.get("honeylocust"), equalTo(19));
	}
	
	@Test
	void getTreesBadRequest(@Autowired WebTestClient webTestClient) {
		
		map = new HashMap<String, Integer>();
		
		map.put("American elm", 3);
		map.put("common hackberry", 1);
		map.put("Japanese zelkova", 13);
		map.put("sweetgum", 1);
		map.put("tree of heaven", 1);
		map.put("English oak", 4);
		map.put("honeylocust", 19);
		map.put("Callery pear", 2);
		map.put("ginkgo", 3);
		map.put("pin oak", 4);
		map.put("hardy rubber tree", 1);
		map.put("Default", 1);
		
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
	
	@org.springframework.boot.test.context.TestConfiguration
	static class TestConfiguration {
		
		@Bean
		public Utilities getUtilities() {
			return new Utilities();
		}
	}
}

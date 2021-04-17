package com.holidu.interview.assignment.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.holidu.interview.assignment.exception.TreesException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@EnableWebFlux
public class TreesConfiguration implements WebFluxConfigurer {

	@Value("${api.tree.data.url}")
	private String baseUrl;
	
	@Bean
	@Qualifier("allTrees")
	public WebClient getAllTreesWebClient() {
		return WebClient.builder()
				.baseUrl(baseUrl)
				.filter(ExchangeFilterFunction.ofResponseProcessor(this::renderApiErrorResponse))
				.build();
	}

	@Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().enableLoggingRequestDetails(true);
    }
	
	private Mono<ClientResponse> renderApiErrorResponse(ClientResponse clientResponse) {
        if(clientResponse.statusCode().isError()){
        	log.error("Error occured while processing the request."+clientResponse.statusCode());
            return clientResponse.bodyToMono(TreesException.class)
                    .flatMap(apiErrorResponse -> Mono.error(new ResponseStatusException(
                            clientResponse.statusCode(),
                            apiErrorResponse.getMessage()
                    )));
        }
        log.debug("Returning response successfully");
        return Mono.just(clientResponse);
    }
}

package com.holidu.interview.assignment.exception;

import java.util.Map;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@SuppressWarnings("deprecation")
@Component
@Order(-2)
@Slf4j
public class TreesExceptionHandler extends AbstractErrorWebExceptionHandler {

    public TreesExceptionHandler(TreesErrorAttributes g, ApplicationContext applicationContext,
            ServerCodecConfigurer serverCodecConfigurer) {
        super(g, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * Customer response error handler for the service.
     * 
     * @param request
     * @return
     */
    private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {

    	log.debug("Error processors");
        final Map<String, Object> errorPropertiesMap = getErrorAttributes(request, false);

        /**
         * Mapping appropriate HttpStatus for the excetions caught from all 
         * over the service.
         */
        if (errorPropertiesMap.get("status").equals(500)) {
        	return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(errorPropertiesMap));
		}
        
        if (errorPropertiesMap.get("status").equals(400)) {
        	return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(errorPropertiesMap));
        }
        
        if (errorPropertiesMap.get("status").equals(429)) {
        	return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(errorPropertiesMap));
        }
        
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(errorPropertiesMap));
        
    }

}
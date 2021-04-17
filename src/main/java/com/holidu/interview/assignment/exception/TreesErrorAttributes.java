package com.holidu.interview.assignment.exception;

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebInputException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TreesErrorAttributes extends DefaultErrorAttributes {
    
	/**
	 * Overriding default attributes with customer error attributes for http errors
	 * caught from all over the appilcation.
	 * 
	 * @param request
	 * @param options
	 */
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(request, options);

        if (getError(request) instanceof TreesException) {
        	log.debug("Exception attribute builder");
            TreesException ex = (TreesException) getError(request);
            map.put("message", ex.getReason());
            map.put("status", ex.getStatus().value());
            map.put("error", ex.getStatus().getReasonPhrase());
        } else if (getError(request) instanceof ServerWebInputException) {
        	log.debug("Exception attribute builder");
        	ServerWebInputException ex = (ServerWebInputException) getError(request);
            map.put("message", ex.getReason());
            map.put("status", ex.getStatus().value());
            map.put("error", ex.getStatus().getReasonPhrase());
        } else if (getError(request) instanceof Exception) {
        	log.debug("Exception attribute builder");
        	Exception ex = (Exception) getError(request);
            map.put("message", ex.getMessage());
            map.put("status", 500);
            map.put("error", ex.getCause());
		}
        return map;
    }
}
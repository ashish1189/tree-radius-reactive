package com.holidu.interview.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TreesException extends ResponseStatusException {

	private static final long serialVersionUID = -1473307282647937444L;

	public TreesException(HttpStatus status, String message) {
        super(status, message);
    }
    
    public TreesException(HttpStatus status, String message, Throwable e) {
        super(status, message, e);
    }
}


package com.holidu.interview.assignment.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.holidu.interview.assignment.exception.TreesException;
import com.holidu.interview.assignment.service.TreesService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class TreesController {

	@Autowired
	private TreesService service;
	
	@GetMapping(path = "/all-trees", produces = "application/json")
	public Mono<Map<String, Integer>> getAllTreesInRadius(
			@RequestParam(name = "x") Double x,
			@RequestParam(name = "y") Double y,
			@RequestParam(name = "radius") Double radius) {

		if (radius == null || radius <= 0) {
			log.error("Invalid radius "+radius);
			throw new TreesException(HttpStatus.BAD_REQUEST, "Radius should be non zero positive");
		}
		
		if (!(x instanceof Double)) {
			log.error("Invalid X coordinate "+x);
			throw new TreesException(HttpStatus.BAD_REQUEST, "X coordinate should be a number");
		}
		
		if (!(y instanceof Double)) {
			log.error("Invalid X coordinate "+y);
			throw new TreesException(HttpStatus.BAD_REQUEST, "Y coordinate should be a number");
		}
		
		return service.getTrees(x, y, radius);
	}
}

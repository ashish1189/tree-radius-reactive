package com.holidu.interview.assignment.service;

import java.util.Map;

import reactor.core.publisher.Mono;

public interface TreesService {
	Mono<Map<String, Integer>> getTrees(double x, double y, double radius);
}

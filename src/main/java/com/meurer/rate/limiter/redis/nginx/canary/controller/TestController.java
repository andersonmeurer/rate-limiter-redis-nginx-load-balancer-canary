package com.meurer.rate.limiter.redis.nginx.canary.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/")
public class TestController {

	private int counter;
	@GetMapping
	public ResponseEntity<String> testEndpoint() {
		counter++;
		log.info("Request processed successfully counter: {}!", counter);
		return ResponseEntity.ok("Request processed successfully!");
	}
}
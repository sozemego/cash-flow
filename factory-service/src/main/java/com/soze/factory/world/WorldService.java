package com.soze.factory.world;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WorldService {

	private static final Logger LOG = LoggerFactory.getLogger(WorldService.class);

	private final String url = "http://localhost:9000/world/mark";
	private final RestTemplate restTemplate = new RestTemplate();

}

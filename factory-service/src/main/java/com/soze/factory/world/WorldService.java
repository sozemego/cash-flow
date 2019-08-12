package com.soze.factory.world;

import java.util.HashMap;
import java.util.Map;

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

@Service
public class WorldService {

  private static final Logger LOG = LoggerFactory.getLogger(WorldService.class);

  private final String url = "http://localhost:9000/world/mark";
  private final RestTemplate restTemplate = new RestTemplate();

  public boolean isTileTaken(int x, int y) {
    LOG.trace("Checking if tile is taken [{}:{}]", x, y);
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, new HttpHeaders());

    ResponseEntity<Boolean> taken = restTemplate
        .exchange(url + "?x={x}&y={y}", HttpMethod.GET, request, Boolean.class, String.valueOf(x),
            String.valueOf(y));
    return taken.getBody();
  }

  public void markAsTaken(int x, int y) {
    LOG.trace("Marking tile [{}:{}] as taken", x, y);
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("x", String.valueOf(x));
    params.add("y", String.valueOf(y));
    params.add("mark", "true");
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, new HttpHeaders());

    restTemplate.postForEntity(url, request, Void.class);
  }

  public void markAsFree(int x, int y) {
    LOG.trace("Marking tile [{}:{}] as free", x, y);
    Map<String, String> params = new HashMap<>();
    params.put("x", String.valueOf(x));
    params.put("y", String.valueOf(y));
    params.put("mark", "false");
    restTemplate.postForEntity(url, null, Void.class, params);
  }

}

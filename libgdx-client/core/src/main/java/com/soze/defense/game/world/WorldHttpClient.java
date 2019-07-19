package com.soze.defense.game.world;

import com.soze.common.dto.WorldDTO;
import com.soze.common.json.JsonUtils;
import com.soze.defense.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorldHttpClient {

  private static final Logger LOG = LoggerFactory.getLogger(WorldHttpClient.class);

  private final HttpClient client = new HttpClient("http://localhost:9000/world");

  public WorldDTO getWorld() {
    String response = client.get();
    WorldDTO world = JsonUtils.parse(response, WorldDTO.class);
    LOG.info("Fetched {}", world);
    return world;
  }

}

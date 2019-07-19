package com.soze.world.service;

import com.soze.common.dto.TileDTO;
import com.soze.common.dto.WorldDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WorldService {

  private static final Logger LOG = LoggerFactory.getLogger(WorldService.class);

  private final int tileWidth = 128;
  private final int tileHeight = 128;
  private List<TileDTO> tiles = new ArrayList<>();

  @PostConstruct
  public void setup() {
    if (!tiles.isEmpty()) {
      LOG.info("World already setup! Skipping init.");
    }

    LOG.info("Init World...");
    for (int i = 0; i < 25; i++) {
      for (int j = 0; j < 25; j++) {
        int x = i * tileWidth;
        int y = j * tileHeight;
        tiles.add(new TileDTO(x, y));
      }
    }
    LOG.info("Created {} tiles", tiles.size());
  }

  public WorldDTO getWorld() {
    return new WorldDTO(tiles, tileWidth, tileHeight);
  }

}
package com.soze.common.dto;

import java.util.List;

public class WorldDTO {

  private final List<TileDTO> tiles;
  private final int tileSize;

  public WorldDTO(List<TileDTO> tiles, int tileSize) {
    this.tiles = tiles;
    this.tileSize = tileSize;
  }

  public List<TileDTO> getTiles() {
    return tiles;
  }

  public int getTileSize() {
    return tileSize;
  }
}

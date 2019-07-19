package com.soze.common.dto;

import java.util.List;

public class WorldDTO {

  private final List<TileDTO> tiles;
  private final int tileWidth;
  private final int tileHeight;

  public WorldDTO(List<TileDTO> tiles, int tileWidth, int tileHeight) {
    this.tiles = tiles;
    this.tileWidth = tileWidth;
    this.tileHeight = tileHeight;
  }

  public List<TileDTO> getTiles() {
    return tiles;
  }

  public int getTileWidth() {
    return tileWidth;
  }

  public int getTileHeight() {
    return tileHeight;
  }

  @Override
  public String toString() {
    return "WorldDTO{" +
        "tiles count=" + tiles.size() +
        ", tileWidth=" + tileWidth +
        ", tileHeight=" + tileHeight +
        '}';
  }
}

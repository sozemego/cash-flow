package com.soze.defense.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.soze.defense.MyAssetManager;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class World {

  private static final Logger LOG = LoggerFactory.getLogger(World.class);

  private final MyAssetManager assetManager;

  private final Map<Vector2, Tile> tiles = new HashMap<>();

  public World(MyAssetManager assetManager) {
    this.assetManager = assetManager;
  }

  public void init() {
    LOG.info("Init World...");
    for (int i = 0; i < 25; i++) {
      for (int j = 0; j < 25; j++) {
        Vector2 position = new Vector2(i * Tile.WIDTH, j * Tile.HEIGHT);
        Sprite sprite = new Sprite(
            assetManager.getTexture("textures/terrain/tile/medievalTile_57.png"));
        sprite.setBounds(position.x, position.y, Tile.WIDTH, Tile.HEIGHT);
        Tile tile = new Tile(position, sprite);
        tiles.put(new Vector2(i, j), tile);
      }
    }
    LOG.info("Created {} tiles", tiles.size());
  }

  /**
   * position needs to be a world position, not position on the screen.
   */
  public Tile getTileAt(Vector2 position) {
    float x = MathUtils.floor(position.x / Tile.WIDTH);
    float y = MathUtils.floor(position.y / Tile.HEIGHT);
    return tiles.get(new Vector2(x, y));
  }

  public void render(SpriteBatch batch, float delta) {
    tiles.values().forEach(tile -> tile.render(batch, delta));
  }

}

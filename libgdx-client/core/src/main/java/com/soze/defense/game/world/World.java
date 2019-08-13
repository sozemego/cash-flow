package com.soze.defense.game.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.soze.common.dto.TileDTO;
import com.soze.common.dto.WorldDTO;
import com.soze.defense.MyAssetManager;
import com.soze.defense.game.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {

	private static final Logger LOG = LoggerFactory.getLogger(World.class);

	private final MyAssetManager assetManager;
	private final WorldHttpClient client;

	private final Map<Vector2, Tile> tiles = new HashMap<>();

	public World(MyAssetManager assetManager, WorldHttpClient client) {
		this.assetManager = assetManager;
		this.client = client;
	}

	public void init() {
		LOG.info("Fetching world...");
		WorldDTO dto = client.getWorld();
		List<TileDTO> tileDTOs = dto.getTiles();
		for (TileDTO tileDTO : tileDTOs) {
			Vector2 position = new Vector2(tileDTO.getX(), tileDTO.getY());
			Sprite sprite = new Sprite(assetManager.getTexture("textures/terrain/tile/medievalTile_57.png"));
			sprite.setBounds(position.x, position.y, Tile.WIDTH, Tile.HEIGHT);
			Tile tile = new Tile(position, sprite);
			tiles.put(getTileIndexPosition(new Vector2(tileDTO.getX(), tileDTO.getY())), tile);
		}
		LOG.info("Fetched {} tiles", tiles.size());
	}

	/**
	 * position needs to be a world position, not position on the screen.
	 */
	public Tile getTileAt(Vector2 position) {
		return tiles.get(getTileIndexPosition(position));
	}

	public Vector2 getTileIndexPosition(Vector2 position) {
		float x = MathUtils.floor(position.x / Tile.WIDTH);
		float y = MathUtils.floor(position.y / Tile.HEIGHT);
		return new Vector2(x, y);
	}

	public void render(SpriteBatch batch, float delta) {
		tiles.values().forEach(tile -> tile.render(batch, delta));
	}

}

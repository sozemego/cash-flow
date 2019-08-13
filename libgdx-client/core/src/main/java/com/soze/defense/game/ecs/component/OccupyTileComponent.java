package com.soze.defense.game.ecs.component;

import com.soze.defense.game.Tile;

public class OccupyTileComponent extends BaseComponent {

	private Tile tile;

	public OccupyTileComponent() {

	}

	public OccupyTileComponent(Tile tile) {
		this.tile = tile;
	}

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}
}

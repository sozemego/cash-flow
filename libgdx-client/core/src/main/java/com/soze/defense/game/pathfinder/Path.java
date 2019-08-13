package com.soze.defense.game.pathfinder;

import com.soze.defense.game.Tile;

import java.util.LinkedList;
import java.util.Optional;

public class Path {

	private final LinkedList<Tile> tiles;

	private Runnable onFinish;

	public Path(LinkedList<Tile> tiles) {
		this.tiles = tiles;
	}

	public LinkedList<Tile> getTiles() {
		return tiles;
	}

	public Optional<Tile> getCurrent() {
		if (tiles.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(tiles.getLast());
	}

	public Runnable getOnFinish() {
		return onFinish;
	}

	public void setOnFinish(Runnable onFinish) {
		this.onFinish = onFinish;
	}

	@Override
	public String toString() {
		return "Path{" + "tiles=" + tiles + '}';
	}

	public void next() {
		if (!tiles.isEmpty()) {
			tiles.removeLast();
		}
	}
}

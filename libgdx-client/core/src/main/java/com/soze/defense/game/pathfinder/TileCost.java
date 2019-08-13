package com.soze.defense.game.pathfinder;

import com.soze.defense.game.Tile;

public class TileCost implements Comparable<TileCost> {

	private final Tile tile;
	private final int cost;

	public TileCost(Tile tile, int cost) {
		this.tile = tile;
		this.cost = cost;
	}

	public Tile getTile() {
		return tile;
	}

	public int getCost() {
		return cost;
	}

	@Override
	public int compareTo(TileCost o) {
		return getCost() - o.getCost();
	}
}

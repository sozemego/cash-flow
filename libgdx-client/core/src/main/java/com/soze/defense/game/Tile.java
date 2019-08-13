package com.soze.defense.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Objects;

public class Tile {

	public static final int WIDTH = 128;
	public static final int HEIGHT = 128;

	private final Vector2 position;
	private final Sprite sprite;

	private boolean hovered = false;

	public Tile(Vector2 position, Sprite sprite) {
		this.position = position;
		this.sprite = sprite;
	}

	public Vector2 getPosition() {
		return position;
	}

	public Vector2 getCenter() {
		return this.sprite.getBoundingRectangle().getCenter(new Vector2());
	}

	public Sprite getSprite() {
		return sprite;
	}

	public boolean isHovered() {
		return hovered;
	}

	public void setHovered(boolean hovered) {
		this.hovered = hovered;
	}

	public void render(SpriteBatch batch, float delta) {
		Color previousColor = sprite.getColor().cpy();

		if (isHovered()) {
			sprite.setColor(previousColor.cpy().mul(1f, 1f, 1f, 0.75f));
		}

		sprite.setSize(Tile.WIDTH, Tile.HEIGHT);
		sprite.setPosition(position.x, position.y);
		sprite.draw(batch);

		sprite.setColor(previousColor);
	}

	@Override
	public String toString() {
		return "Tile{" +
			"position=" + position +
			", hovered=" + hovered +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Tile tile = (Tile) o;
		return Objects.equals(position, tile.position);
	}

	@Override
	public int hashCode() {
		return Objects.hash(position);
	}
}

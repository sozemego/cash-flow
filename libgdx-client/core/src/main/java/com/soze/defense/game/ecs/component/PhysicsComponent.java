package com.soze.defense.game.ecs.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PhysicsComponent extends BaseComponent {

	private final Vector2 position;
	private final Vector2 size;

	public PhysicsComponent(Vector2 position, Vector2 size) {
		this.position = position;
		this.size = size;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(float x, float y) {
		this.position.set(x, y);
	}

	public Vector2 getSize() {
		return size;
	}

	public void setSize(float x, float y) {
		this.size.set(x, y);
	}

	public boolean isPointIn(Vector2 point) {
		return new Rectangle(
			position.x - size.x / 2, position.y - size.y / 2, size.x, size.y)
			.contains(point);
	}

	@Override
	public String toString() {
		return "PhysicsComponent{" +
			"position=" + position +
			", size=" + size +
			'}';
	}
}

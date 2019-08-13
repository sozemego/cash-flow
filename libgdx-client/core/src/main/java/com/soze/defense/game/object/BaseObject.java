package com.soze.defense.game.object;

import com.badlogic.gdx.math.Vector2;

public abstract class BaseObject {

	private final String id;
	private final String name;
	private final Vector2 position;
	private final Vector2 size;

	public BaseObject(String id, String name, Vector2 position, Vector2 size) {
		this.id = id;
		this.name = name;
		this.position = position;
		this.size = size;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Vector2 getPosition() {
		return position;
	}

	public Vector2 getSize() {
		return size;
	}
}

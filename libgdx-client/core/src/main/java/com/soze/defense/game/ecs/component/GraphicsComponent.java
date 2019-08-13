package com.soze.defense.game.ecs.component;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class GraphicsComponent extends BaseComponent {

	private final Sprite sprite;

	public GraphicsComponent(Sprite sprite) {
		this.sprite = sprite;
	}

	public Sprite getSprite() {
		return sprite;
	}
}

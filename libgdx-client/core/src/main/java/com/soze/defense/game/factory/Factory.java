package com.soze.defense.game.factory;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.soze.defense.game.Renderer;
import com.soze.defense.game.object.BaseObject;

public class Factory extends BaseObject {

	private final Sprite sprite;

	private final Producer producer;
	private final Storage storage;

	public Factory(String id, String name, Vector2 position, Vector2 size, Sprite sprite, Producer producer,
								 Storage storage
								) {
		super(id, name, position, size);
		this.sprite = sprite;
		this.producer = producer;
		this.storage = storage;
	}

	public Producer getProducer() {
		return producer;
	}

	public Storage getStorage() {
		return storage;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void update(float delta) {
		Producer producer = getProducer();
		if (producer.isProducing()) {
			float progress = producer.getProgress();
			float nextProgress = progress + (delta * 1000);
			producer.setProgress(MathUtils.clamp(nextProgress, 0, producer.getTime()));
		}
	}

	public void render(Renderer renderer) {
		renderer.render(this);
	}
}

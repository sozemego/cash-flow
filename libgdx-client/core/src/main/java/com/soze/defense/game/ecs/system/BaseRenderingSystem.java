package com.soze.defense.game.ecs.system;

import com.soze.klecs.engine.Engine;

public abstract class BaseRenderingSystem extends BaseEntitySystem {

	public BaseRenderingSystem(Engine engine) {
		super(engine);
	}

	@Override
	public boolean isRenderer() {
		return true;
	}
}

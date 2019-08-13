package com.soze.defense.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import java.util.HashSet;
import java.util.Set;

public class FluidCamera extends OrthographicCamera implements InputProcessor {

	private final Set<Integer> pressedKeys = new HashSet<>();

	@Override
	public void update() {
		if (pressedKeys.contains(Input.Keys.A)) {
			translate(-5, 0, 0);
		}
		if (pressedKeys.contains(Input.Keys.D)) {
			translate(5, 0, 0);
		}
		if (pressedKeys.contains(Input.Keys.W)) {
			translate(0, 5, 0);
		}
		if (pressedKeys.contains(Input.Keys.S)) {
			translate(0, -5, 0);
		}

		super.update();
	}

	@Override
	public boolean keyDown(int keycode) {
		pressedKeys.add(keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		pressedKeys.remove(keycode);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		float zoomChange = amount > 0 ? 0.05f : -0.05f;
		zoom = MathUtils.clamp(zoom + zoomChange, 0.5f, 15f);
		return false;
	}
}

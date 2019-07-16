package com.soze.defense.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MousePointer {

  private final Camera camera;

  public MousePointer(Camera camera) {
    this.camera = camera;
  }

  /**
   * Returns Vector2 containing current mouse coordinates. X-axis is pointing right, y-axis is
   * pointing up.
   */
  public Vector2 getMouseScreenCoordinates() {
    float x = Gdx.input.getX();
    float y = Gdx.graphics.getHeight() - Gdx.input.getY();
    return new Vector2(x, y);
  }

  public Vector2 getRawMouseCoordinates() {
    float x = Gdx.input.getX();
    float y = Gdx.input.getY();
    return new Vector2(x, y);
  }

  /**
   * Returns mouse position in the world.
   */
  public Vector2 getMouseWorldCoordinates() {
    Vector2 screenCoordinates = getRawMouseCoordinates();
    Vector3 unprojectedCoordinates = camera
        .unproject(new Vector3(screenCoordinates.x, screenCoordinates.y, 0f));
    return new Vector2(unprojectedCoordinates.x, unprojectedCoordinates.y);
  }

}

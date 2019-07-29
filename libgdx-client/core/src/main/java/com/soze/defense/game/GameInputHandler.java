package com.soze.defense.game;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.soze.defense.game.world.World;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameInputHandler implements InputProcessor {

  private static final Logger LOG = LoggerFactory.getLogger(GameInputHandler.class);

  private final Set<Integer> pressedKeys = new HashSet<>();

  private final ScreenViewport viewport;
  private final Game game;
  private final World world;

  private Tile hoveredTile;

  public GameInputHandler(ScreenViewport viewport, Game game, World world) {
    this.viewport = viewport;
    this.game = game;
    this.world = world;
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
    if (pressedKeys.contains(Keys.L) && button == Buttons.LEFT) {

    }
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    Tile tile = world.getTileAt(getGameCoords(screenX, screenY));
    if (hoveredTile != null) {
      hoveredTile.setHovered(false);
    }
    if (tile != null) {
      tile.setHovered(true);
      hoveredTile = tile;
    }
    LOG.trace("Mouse is over tile {}", tile);
    return false;
  }

  @Override
  public boolean scrolled(int amount) {
    return false;
  }

  private Vector2 getGameCoords(int screenX, int screenY) {
    Camera camera = viewport.getCamera();
    Vector3 unprojected = camera.unproject(new Vector3(screenX, screenY, 0));
    return new Vector2(unprojected.x, unprojected.y);
  }
}

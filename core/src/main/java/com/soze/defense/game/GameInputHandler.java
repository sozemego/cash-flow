package com.soze.defense.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameInputHandler implements InputProcessor {

  private static final Logger LOG = LoggerFactory.getLogger(GameInputHandler.class);

  private final ScreenViewport viewport;
  private final GameService gameService;
  private final World world;

  private Tile hoveredTile;

  public GameInputHandler(ScreenViewport viewport, GameService gameService, World world) {
    this.viewport = viewport;
    this.gameService = gameService;
    this.world = world;
  }

  @Override
  public boolean keyDown(int keycode) {
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
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
    Camera camera = viewport.getCamera();
    Vector3 unprojected = camera.unproject(new Vector3(screenX, screenY, 0));
    Tile tile = world.getTileAt(new Vector2(unprojected.x, unprojected.y));
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
}

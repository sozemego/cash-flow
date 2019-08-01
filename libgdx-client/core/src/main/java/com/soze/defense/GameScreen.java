package com.soze.defense;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.soze.defense.game.Game;
import com.soze.defense.game.GameInputHandler;
import com.soze.defense.game.Renderer;
import com.soze.defense.game.world.World;
import com.soze.defense.game.world.WorldHttpClient;
import com.soze.defense.input.FluidCamera;
import com.soze.defense.input.MousePointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GameScreen implements Screen {

  private final static Logger LOG = LoggerFactory.getLogger(GameScreen.class);

  private final FluidCamera camera = new FluidCamera();
  private final ScreenViewport viewport = new ScreenViewport(camera);

  private final SpriteBatch batch = new SpriteBatch();

  private final InputMultiplexer inputMultiplexer = new InputMultiplexer();

  private final MyAssetManager myAssetManager;

  private final World world;
  private final Game game;
  private final GameInputHandler gameInputHandler;
  private final MousePointer mousePointer = new MousePointer(camera);

  private final Renderer renderer;

  public GameScreen(MyAssetManager assetManager) {
    this.myAssetManager = assetManager;

    this.world = new World(this.myAssetManager, new WorldHttpClient());
    this.game = new Game(this.world, this.myAssetManager, this.batch,
        this.mousePointer);
    this.game.init();

    this.gameInputHandler = new GameInputHandler(this.viewport, this.game, this.world);

    inputMultiplexer.addProcessor(this.gameInputHandler);
    inputMultiplexer.addProcessor(this.camera);
    Gdx.input.setInputProcessor(this.inputMultiplexer);

    this.renderer = new Renderer(batch, camera, mousePointer, assetManager);
  }

  @Override
  public void show() {
    LOG.info("Showing {}", this.getClass().getSimpleName());

    camera.zoom = 1f;
    camera.keyUp(Input.Keys.A);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    camera.update();
    batch.setProjectionMatrix(camera.combined);

    game.update(delta);

    batch.enableBlending();
    batch.begin();
    world.render(batch, delta);
    game.render(renderer);
    batch.end();
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height, true);
  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {
    this.inputMultiplexer.clear();
    Gdx.input.setInputProcessor(null);
  }
}

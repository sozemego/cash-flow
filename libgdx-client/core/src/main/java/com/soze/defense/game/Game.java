package com.soze.defense.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.soze.defense.MyAssetManager;
import com.soze.defense.game.factory.FactoryService;
import com.soze.defense.game.factory.FactoryWebSocketClient;
import com.soze.defense.game.world.World;
import com.soze.defense.input.MousePointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {

  private static final Logger LOG = LoggerFactory.getLogger(Game.class);

  private final MyAssetManager assetManager;

  private final World world;
  private final ObjectFactory objectFactory;
  private final SpriteBatch batch;
  private final MousePointer mousePointer;

  //Services section
  private final FactoryService factoryService;

  public Game(World world, MyAssetManager assetManager, SpriteBatch batch,
              MousePointer mousePointer) {
    this.world = world;
    this.assetManager = assetManager;

    this.objectFactory = new ObjectFactory(assetManager, world);
    this.batch = batch;
    this.mousePointer = mousePointer;

    FactoryWebSocketClient factoryWebSocketClient = FactoryWebSocketClient
        .create("ws://localhost:9001/factory/websocket");

    this.factoryService = new FactoryService(factoryWebSocketClient,
        objectFactory);
  }

  public void init() {
    objectFactory.loadEntityTemplates();
    world.init();
    factoryService.init();
//    engine.addSystem(new ResourceProducerSystem(engine, this, new PathFinder(this)));
//    engine.addSystem(new PathFollowerSystem(engine));
//    engine.addSystem(new PathFollowerRenderingSystem(engine, batch, assetManager));
//    engine.addSystem(new GraphicsSystem(engine, batch));

  }

  public FactoryService getFactoryService() {
    return factoryService;
  }

  public void update(float delta) {
    factoryService.update(delta);
  }

  public void render(Renderer renderer) {
    factoryService.render(renderer);
  }

}

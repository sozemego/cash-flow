package com.soze.defense.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.soze.defense.MyAssetManager;
import com.soze.defense.game.factory.Factory;
import com.soze.defense.game.factory.Producer;
import com.soze.defense.game.factory.Storage;
import com.soze.defense.game.ui.ProductionProgressIndicator;
import com.soze.defense.input.MousePointer;

public class Renderer {

  private final SpriteBatch spriteBatch;
  private final Camera camera;
  private final MousePointer mousePointer;
  private final MyAssetManager assetManager;

  private final BitmapFont font = new BitmapFont();

  public Renderer(SpriteBatch spriteBatch, Camera camera,
                  MousePointer mousePointer, MyAssetManager assetManager) {
    this.spriteBatch = spriteBatch;
    this.camera = camera;
    this.mousePointer = mousePointer;
    this.assetManager = assetManager;
  }

  public void render(Factory factory) {
    Vector2 position = factory.getPosition();
    Vector2 size = factory.getSize();
    Sprite sprite = factory.getSprite();
    sprite.setBounds(position.x - size.x / 2, position.y - size.y / 2, size.x, size.y);
    sprite.draw(spriteBatch);

    Storage storage = factory.getStorage();
    String capacityTaken = String.valueOf(storage.getCapacityTaken());
    String capacity = String.valueOf(storage.getCapacity());

    font.setColor(Color.WHITE);
    font.draw(spriteBatch, capacityTaken + "/" + capacity, position.x - size.x / 2,
        position.y - size.y / 2);

    Producer producer = factory.getProducer();
    Sprite resourceSprite = assetManager.getResourceSprite(producer.getResource());
    ProductionProgressIndicator productionProgressIndicator = new ProductionProgressIndicator(
        factory, resourceSprite, font);
    productionProgressIndicator.render(spriteBatch, true);
  }

}

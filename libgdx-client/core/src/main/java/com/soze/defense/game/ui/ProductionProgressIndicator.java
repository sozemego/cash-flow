package com.soze.defense.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.soze.defense.game.ecs.component.PhysicsComponent;
import com.soze.defense.game.ecs.component.ResourceProducerComponent;
import com.soze.defense.game.factory.Factory;
import com.soze.defense.game.factory.Producer;
import com.soze.klecs.entity.Entity;
import java.text.DecimalFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductionProgressIndicator {

  private static final Logger LOG = LoggerFactory.getLogger(ProductionProgressIndicator.class);

  private final Factory factory;
  private final Sprite resourceSprite;
  private final BitmapFont font;

  public ProductionProgressIndicator(Factory factory, Sprite resourceSprite, BitmapFont font) {
    this.factory = factory;
    this.resourceSprite = resourceSprite;
    this.font = font;
  }

  public void render(SpriteBatch batch, boolean mouseOver) {
    Vector2 position = factory.getPosition();
    Vector2 size = factory.getSize();

    Producer producer = factory.getProducer();

    float progress = producer.getProgress() / 1000f;
    float time = producer.getTime() / 1000f;
    float percent = progress / time;

    this.resourceSprite.setBounds(position.x - size.x / 2, position.y + size.y / 2, 32, 32);
    this.resourceSprite.draw(batch, percent < 0.75f ? 0.75f : percent);

    if (mouseOver) {
      this.font.setColor(Color.WHITE);
      this.font.draw(batch,
          String.format("%.1f", progress) + "/" + new DecimalFormat("#.#").format(time) + " s",
          position.x - (size.x / 2) + 32, position.y + (size.y / 2) + 22);
    }
  }

}

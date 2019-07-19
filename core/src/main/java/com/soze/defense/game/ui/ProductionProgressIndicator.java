package com.soze.defense.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.soze.defense.game.ecs.component.PhysicsComponent;
import com.soze.defense.game.ecs.component.ResourceProducerComponent;
import com.soze.klecs.entity.Entity;
import java.text.DecimalFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductionProgressIndicator {

  private static final Logger LOG = LoggerFactory.getLogger(ProductionProgressIndicator.class);

  private final Entity entity;
  private final Sprite resourceSprite;
  private final BitmapFont font;

  public ProductionProgressIndicator(Entity entity, Sprite resourceSprite, BitmapFont font) {
    this.entity = entity;
    this.resourceSprite = resourceSprite;
    this.font = font;
  }

  public void update(SpriteBatch batch, float delta, boolean mouseOver) {
    PhysicsComponent physicsComponent = entity.getComponent(PhysicsComponent.class);
    Vector2 position = physicsComponent.getPosition();
    Vector2 size = physicsComponent.getSize();

    ResourceProducerComponent resourceProducerComponent = entity
        .getComponent(ResourceProducerComponent.class);

    float progress = resourceProducerComponent.getProgress();
    float time = resourceProducerComponent.getTime();
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

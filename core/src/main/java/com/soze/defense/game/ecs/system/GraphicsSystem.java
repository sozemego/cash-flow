package com.soze.defense.game.ecs.system;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.soze.defense.game.ecs.NodeHelper;
import com.soze.defense.game.ecs.component.GraphicsComponent;
import com.soze.defense.game.ecs.component.PhysicsComponent;
import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;

public class GraphicsSystem extends BaseRenderingSystem {

  private final SpriteBatch batch;

  public GraphicsSystem(Engine engine, SpriteBatch batch) {
    super(engine);
    this.batch = batch;
  }

  @Override
  public void update(float delta) {
    getEngine()
        .getEntitiesByNode(NodeHelper.GRAPHICS)
        .forEach(entity -> update(entity, delta));
  }

  private void update(Entity entity, float delta) {
    GraphicsComponent graphicsComponent = entity.getComponent(GraphicsComponent.class);
    PhysicsComponent physicsComponent = entity.getComponent(PhysicsComponent.class);
    Sprite sprite = graphicsComponent.getSprite();
    Vector2 position = physicsComponent.getPosition();
    Vector2 size = physicsComponent.getSize();
    sprite.setBounds(position.x, position.y, size.x, size.y);

    sprite.draw(batch);
  }

}

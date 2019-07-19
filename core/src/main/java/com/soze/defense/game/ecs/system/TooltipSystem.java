package com.soze.defense.game.ecs.system;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.soze.defense.MyAssetManager;
import com.soze.defense.game.ecs.NodeHelper;
import com.soze.defense.game.ecs.component.BaseStorage;
import com.soze.defense.game.ecs.component.PhysicsComponent;
import com.soze.defense.game.ecs.component.ResourceProducerComponent;
import com.soze.defense.game.ecs.component.StorageComponent;
import com.soze.defense.game.ecs.component.TooltipComponent;
import com.soze.defense.game.ui.ProductionProgressIndicator;
import com.soze.defense.input.MousePointer;
import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TooltipSystem extends BaseRenderingSystem {

  private static final Logger LOG = LoggerFactory.getLogger(TooltipSystem.class);

  private final BitmapFont font = new BitmapFont();

  private final SpriteBatch batch;
  private final MyAssetManager assetManager;
  private final MousePointer mousePointer;

  public TooltipSystem(Engine engine, SpriteBatch batch, MyAssetManager assetManager,
      MousePointer mousePointer) {
    super(engine);
    this.batch = batch;
    this.assetManager = assetManager;
    this.mousePointer = mousePointer;
  }

  @Override
  public void update(float delta) {
    getEngine().getEntitiesByNode(NodeHelper.TOOLTIP).forEach(entity -> update(entity, delta));
  }

  private void update(Entity entity, float delta) {
    PhysicsComponent physicsComponent = entity.getComponent(PhysicsComponent.class);
    TooltipComponent tooltipComponent = entity.getComponent(TooltipComponent.class);

    Vector2 position = physicsComponent.getPosition();
    Vector2 size = physicsComponent.getSize();

    Vector2 mouse = mousePointer.getMouseWorldCoordinates();
    boolean mouseOver = physicsComponent.isPointIn(mouse);

    BaseStorage storageComponent = entity.getComponentByParent(BaseStorage.class);
    if (storageComponent != null && mouseOver) {
      String capacityTaken = String.valueOf(storageComponent.getCapacityTaken());
      String capacity = String.valueOf(storageComponent.getCapacity());

      font.setColor(Color.WHITE);
      font.draw(batch, capacityTaken + "/" + capacity, position.x - size.x / 2, position.y - size.y / 2);
    }

    ResourceProducerComponent resourceProducerComponent = entity
        .getComponent(ResourceProducerComponent.class);
    if (resourceProducerComponent != null && resourceProducerComponent.getProgress() > 0f) {
      createResourceProducerSprite(entity);
      ProductionProgressIndicator progressBar = tooltipComponent.getProductionProgressIndicator();
      progressBar.update(batch, delta, mouseOver);
    }

  }

  private void createResourceProducerSprite(Entity entity) {
    TooltipComponent tooltipComponent = entity.getComponent(TooltipComponent.class);
    ResourceProducerComponent resourceProducerComponent = entity
        .getComponent(ResourceProducerComponent.class);
    if (tooltipComponent.getProductionProgressIndicator() == null) {
      String resourceName = resourceProducerComponent.getResource().toString().toLowerCase();
      Sprite resourceSprite = new Sprite(
          assetManager.getTexture("textures/resources/" + resourceName + ".png"));
      ProductionProgressIndicator productionProgressIndicator = new ProductionProgressIndicator(
          entity, resourceSprite, font);
      tooltipComponent.setProductionProgressIndicator(productionProgressIndicator);
    }
  }

}

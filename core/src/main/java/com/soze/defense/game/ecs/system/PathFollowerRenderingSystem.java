package com.soze.defense.game.ecs.system;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.soze.defense.MyAssetManager;
import com.soze.defense.game.Tile;
import com.soze.defense.game.ecs.NodeHelper;
import com.soze.defense.game.ecs.component.PathFollowerComponent;
import com.soze.defense.game.ecs.component.PhysicsComponent;
import com.soze.defense.game.pathfinder.Path;
import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PathFollowerRenderingSystem extends BaseRenderingSystem {

  private final SpriteBatch batch;
  private final MyAssetManager assetManager;

  private final Sprite bar;

  public PathFollowerRenderingSystem(Engine engine, SpriteBatch batch,
      MyAssetManager assetManager) {
    super(engine);
    this.batch = batch;
    this.assetManager = assetManager;

    this.bar = new Sprite(assetManager.getTexture("textures/ui/white_background.png"));
  }

  @Override
  public void update(float delta) {
    getEngine().getEntitiesByNode(NodeHelper.PATH_FOLLOWER)
               .stream()
               .forEach(entity -> update(entity, delta));
  }

  private void update(Entity entity, float delta) {
    PhysicsComponent physicsComponent = entity.getComponent(PhysicsComponent.class);
    PathFollowerComponent pathFollowerComponent = entity.getComponent(PathFollowerComponent.class);

    Vector2 position = physicsComponent.getPosition();

    Path path = pathFollowerComponent.getPath();
    LinkedList<Tile> tiles = path.getTiles();

    if (tiles.size() <= 1) {
      return;
    }

    List<Vector2> centers = tiles.stream().map(Tile::getCenter)
                                 .collect(Collectors.toList());
    centers.add(position);

    Vector2 from = centers.get(centers.size() - 1);
    Vector2 to = centers.get(0);

    if (centers.size() == 2) {
      drawBar(from, to);
      return;
    }

    drawBars(centers);
  }

  private void drawBar(Vector2 from, Vector2 to) {
    float angle = to.cpy().sub(from).angle();
    float distance = from.dst(to);
    this.bar.setSize(distance, 8);
    this.bar.setPosition(from.x, from.y);
    this.bar.setRotation(angle);
    this.bar.setColor(Color.YELLOW);
    System.out.println("From " + from);
    System.out.println("to " + to);
    System.out.println("angle " + angle);
    Rectangle rect = bar.getBoundingRectangle();
    System.out.println("Rect " + rect);
    System.out.println();
    if (rect.width == 8) {
      bar.setPosition(to.x, to.y);
    }
//    if (angle < 90) {
//      this.bar.setOrigin(this.bar.getWidth() / 2, this.bar.getHeight() / 2);
//    }
//    if (angle >= 90 && angle < 180) {
//      this.bar.setOrigin(0, 0);
//    }
//    if (angle >= 180 && angle < 270) {
//      this.bar.setOrigin(this.bar.getWidth() / 2, this.bar.getHeight() / 2);
//    }
//    if (angle >= 270) {
//      this.bar.setOrigin(0f, 0f);
//    }
    batch.draw(bar.getTexture(), from.x - 4, from.y - 4);
    this.bar.draw(batch, 0.5f);
  }

  private void drawBars(List<Vector2> tiles) {
    Vector2 from = tiles.get(tiles.size() - 1);
    for (int i = tiles.size() - 1; i >= 0; i--) {
      Vector2 to = tiles.get(i);
      drawBar(from, to);
      from = to;
    }
  }
}

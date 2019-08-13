package com.soze.defense.game.ecs.system;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.soze.defense.game.Tile;
import com.soze.defense.game.ecs.NodeHelper;
import com.soze.defense.game.ecs.component.PathFollowerComponent;
import com.soze.defense.game.ecs.component.PhysicsComponent;
import com.soze.defense.game.pathfinder.Path;
import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PathFollowerSystem extends BaseEntitySystem {

	private static final Logger LOG = LoggerFactory.getLogger(
		PathFollowerSystem.class);

	public PathFollowerSystem(Engine engine) {
		super(engine);
	}

	@Override
	public void update(float delta) {
		getEngine().getEntitiesByNode(NodeHelper.PATH_FOLLOWER)
							 .forEach(entity -> update(entity, delta));
	}

	private void update(Entity entity, float delta) {
		PhysicsComponent physicsComponent = entity.getComponent(
			PhysicsComponent.class);
		PathFollowerComponent pathFollowerComponent = entity.getComponent(
			PathFollowerComponent.class);

		Vector2 position = physicsComponent.getPosition();

		Path path = pathFollowerComponent.getPath();

		path.getCurrent().ifPresent(tile -> {
			float speed = Tile.WIDTH / 4;
			Vector2 targetCenter = tile.getCenter();
			float radians = MathUtils.atan2(
				targetCenter.y - position.y, targetCenter.x - position.x);

			float xChange = MathUtils.cos(radians) * speed * delta;
			float yChange = MathUtils.sin(radians) * speed * delta;

			physicsComponent.getPosition().add(xChange, yChange);

			if (MathUtils.isEqual(position.x, targetCenter.x, 5f)
				&& MathUtils.isEqual(position.y, targetCenter.y, 5f)) {
				path.next();
				if (path.getTiles().isEmpty()) {
					getEngine().removeEntity(entity.getId());
					if (path.getOnFinish() != null) {
						path.getOnFinish().run();
					}
				}
			}
		});

	}

}

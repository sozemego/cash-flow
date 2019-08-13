package com.soze.defense.game.ecs;

import com.soze.defense.game.ecs.component.*;
import com.soze.klecs.entity.Entity;
import com.soze.klecs.node.Node;

import java.util.function.Predicate;

public interface NodeHelper {

	Node GRAPHICS = Node.of(PhysicsComponent.class, GraphicsComponent.class);
	Node TOOLTIP = Node.of(PhysicsComponent.class, TooltipComponent.class);
	Node RESOURCE_PRODUCER = Node.of(
		PhysicsComponent.class, ResourceProducerComponent.class);
	Node PATH_FOLLOWER = Node
		.of(
			PhysicsComponent.class, GraphicsComponent.class,
			PathFollowerComponent.class
			 );
	Node OCCUPY_TILE = Node.of(PhysicsComponent.class, OccupyTileComponent.class);

	Predicate<Entity> HAS_STORAGE = entity -> {
		return entity.getComponentByParent(BaseStorage.class) != null;
	};
	Predicate<Entity> ACCEPTS_RESOURCE_PREDICATE = HAS_STORAGE;
}

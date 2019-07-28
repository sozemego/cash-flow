package com.soze.defense.game.ecs.component;

import com.soze.common.dto.Resource;
import java.util.HashMap;
import java.util.Map;

public class BaseStorage extends BaseComponent {

  private final int capacity;
  private final Map<Resource, Integer> resources = new HashMap<>();

  private int capacityTaken = 0;

  public BaseStorage(int capacity) {
    this.capacity = capacity;
  }

  public void addResource(Resource resource) {
    if (!canFit(resource)) {
      return;
    }
    resources.compute(resource, (res, count) -> {
      if (count == null) {
        return 1;
      }
      return ++count;
    });
    capacityTaken += 1;
  }

  public boolean canFit(Resource resource) {
    return capacityTaken < capacity;
  }

  public void removeResource(Resource resource) {
    if (!hasResource(resource)) {
      return;
    }

    resources.compute(resource, (res, count) -> {
      if (count > 0) {
        return --count;
      }
      return count;
    });
    capacityTaken -= 1;
  }

  public boolean hasResource(Resource resource) {
    return resources.getOrDefault(resource, 0) > 0;
  }

  public int getCapacity() {
    return capacity;
  }

  public int getCapacityTaken() {
    return capacityTaken;
  }

  public boolean isFull() {
    return getCapacity() == getCapacityTaken();
  }

  public int getRemainingCapacity() {
    return getCapacity() - getCapacityTaken();
  }

  @Override
  public String toString() {
    return "BaseStorage{" + "capacity=" + capacity + ", resources=" + resources + ", capacityTaken="
        + capacityTaken + '}';
  }
}

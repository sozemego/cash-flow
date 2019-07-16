package com.soze.defense.game.ecs.component;

import com.soze.defense.game.pathfinder.Path;

public class PathFollowerComponent extends BaseComponent {

  private Path path;

  public PathFollowerComponent() {

  }

  public PathFollowerComponent(Path path) {
    this.path = path;
  }

  public Path getPath() {
    return path;
  }

  public void setPath(Path path) {
    this.path = path;
  }
}

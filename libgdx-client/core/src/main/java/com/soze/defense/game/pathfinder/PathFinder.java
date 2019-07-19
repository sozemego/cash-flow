package com.soze.defense.game.pathfinder;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.soze.defense.game.GameService;
import com.soze.defense.game.Tile;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class PathFinder {

  private final GameService gameService;
  private final boolean allowDiagonals = false;

  public PathFinder(GameService gameService) {
    this.gameService = gameService;
  }

  public Path findPath(Tile from, Tile to) {
    PriorityQueue<TileCost> frontier = new PriorityQueue<>();
    frontier.add(new TileCost(from, 0));
    Map<Tile, Tile> cameFrom = new HashMap<>();
    Map<Tile, Integer> costSoFar = new HashMap<>();
    costSoFar.put(from, 0);
    cameFrom.put(from, null);

    while (!frontier.isEmpty()) {
      TileCost tileCost = frontier.poll();

      if (tileCost == null) {
        break;
      }
      Tile current = tileCost.getTile();

      if (current == to) {
        break;
      }

      List<Tile> neighbors = gameService.getNeighbours(current, allowDiagonals)
                                        .stream()
                                        .filter(tile -> tile == to || gameService.isTileFree(tile))
                                        .collect(Collectors.toList());
      for (Tile next : neighbors) {
        int newCost = costSoFar.getOrDefault(current, 0);
        Integer previousCost = costSoFar.get(next);

        if (previousCost == null || newCost < costSoFar.getOrDefault(next, 0)) {
          costSoFar.put(next, newCost);
          int priority = newCost + heuristic(next, to);
          frontier.add(new TileCost(next, priority));
          cameFrom.put(next, current);
        }
      }
    }

    Tile current = to;
    LinkedList<Tile> path = new LinkedList<>();

    while (current != from) {
      path.add(current);
      current = cameFrom.get(current);
    }

    path.add(from);

    return new Path(path);
  }

  private int heuristic(Tile next, Tile to) {
    Vector2 nextPosition = next.getPosition();
    Vector2 toPosition = to.getPosition();
    Vector2 nextIndex = new Vector2(MathUtils.floor(nextPosition.x / Tile.WIDTH),
        MathUtils.floor(nextPosition.y / Tile.HEIGHT));
    Vector2 toIndex = new Vector2(MathUtils.floor(toPosition.x / Tile.WIDTH),
        MathUtils.floor(toPosition.y / Tile.HEIGHT));
    return (int) (Math.abs(nextIndex.x - toIndex.x) + (int) Math.abs(nextIndex.y - toIndex.y));
  }

  private int cost(Tile from, Tile to) {
    return 0;
  }

}

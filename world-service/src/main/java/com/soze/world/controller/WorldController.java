package com.soze.world.controller;

import com.soze.common.dto.WorldDTO;
import com.soze.world.service.WorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorldController {

  private static final Logger LOG = LoggerFactory.getLogger(WorldController.class);

  private final WorldService worldService;

  public WorldController(WorldService worldService) {
    this.worldService = worldService;
  }

  @GetMapping(value = "/")
  public WorldDTO getWorld() {
    LOG.info("Calling getWorld");
    WorldDTO worldDTO = worldService.getWorld();
    LOG.info("Returning world {}", worldDTO);
    return worldService.getWorld();
  }

  @PostMapping(value = "/mark")
  public void markTileAsTaken(@RequestParam("x") int x, @RequestParam("y") int y,
                              @RequestParam("mark") boolean taken) {
    if (taken) {
      worldService.markAsTaken(x, y);
    } else {
      worldService.markAsFree(x, y);
    }
  }

  @GetMapping(value = "/mark")
  public boolean isTileTaken(@RequestParam("x") int x, @RequestParam("y") int y) {
    return worldService.isTileTaken(x, y);
  }

}

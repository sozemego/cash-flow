package com.soze.world.controller;

import com.soze.common.dto.WorldDTO;
import com.soze.world.service.WorldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "World")
public class WorldController {

	private static final Logger LOG = LoggerFactory.getLogger(WorldController.class);

	private final WorldService worldService;

	public WorldController(WorldService worldService) {
		this.worldService = worldService;
	}

	@GetMapping(value = "/")
	@ApiOperation(value = "Retrieves the entire world")
	public WorldDTO getWorld() {
		LOG.info("Calling getWorld");
		WorldDTO worldDTO = worldService.getWorld();
		LOG.info("Returning world {}", worldDTO);
		return worldDTO;
	}

	@PostMapping(value = "/mark")
	@ApiOperation(value = "Marks a given tile (at x:y coordinates) as taken or free")
	public void markTileAsTaken(@RequestParam("x") int x, @RequestParam("y") int y, @RequestParam("mark") boolean taken) {
		if (taken) {
			worldService.markAsTaken(x, y);
		} else {
			worldService.markAsFree(x, y);
		}
	}

	@GetMapping(value = "/mark")
	@ApiOperation(value = "Checks whether a tile at given x:y coordinate is taken")
	public boolean isTileTaken(@RequestParam("x") int x, @RequestParam("y") int y) {
		return worldService.isTileTaken(x, y);
	}

}

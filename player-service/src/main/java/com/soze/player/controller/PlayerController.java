package com.soze.player.controller;

import com.soze.common.client.PlayerServiceClient;
import com.soze.common.dto.PlayerDTO;
import com.soze.player.domain.Player;
import com.soze.player.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController implements PlayerServiceClient {

	private static final Logger LOG = LoggerFactory.getLogger(PlayerController.class);

	private final PlayerService playerService;

	@Autowired
	public PlayerController(PlayerService playerService) {
		this.playerService = playerService;
	}

	@Override
	public PlayerDTO getPlayer() {
		LOG.info("Called getPlayer");
		Player player = playerService.getPlayer();
		PlayerDTO playerDTO = new PlayerDTO(player.getId(), player.getName(), player.getCash());
		LOG.info("returning player {}", playerDTO);
		return playerDTO;
	}
}

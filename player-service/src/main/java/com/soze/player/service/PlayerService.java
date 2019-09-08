package com.soze.player.service;

import com.soze.player.domain.Player;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

	private final Player player = new Player("playerId", "Great owner!", 500);

	public Player getPlayer() {
		return player;
	}

}

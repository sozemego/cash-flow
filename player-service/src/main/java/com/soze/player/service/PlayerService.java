package com.soze.player.service;

import com.soze.player.domain.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

	private static final Logger LOG = LoggerFactory.getLogger(PlayerService.class);

	private final Player player = new Player("playerId", "Great owner!", 500);

	public Player getPlayer() {
		return player;
	}

	public long transfer(long amount) {
		LOG.info("Trying to transfer amount = {}, player has = {}", amount, player.getCash());
		if (amount > 0) {
			player.setCash(player.getCash() + amount);
			LOG.info("Transferred {}, player has {}", amount, player.getCash());
			return amount;
		}

		if (player.getCash() < Math.abs(amount)) {
			LOG.info("Not enough cash to transfer {}, player has {}", amount, player.getCash());
			return 0;
		}

		player.setCash(player.getCash() + amount);
		LOG.info("Transferred {}, player has {}", amount, player.getCash());
		return amount;
	}

}

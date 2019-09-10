package com.soze.truck.external;

import com.soze.common.client.PlayerServiceClient;
import com.soze.common.dto.PlayerDTO;
import com.soze.common.dto.TransferResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Service
public class RemotePlayerService {

	private static final Logger LOG = LoggerFactory.getLogger(RemoteFactoryService.class);

	private final PlayerServiceClient client;

	public RemotePlayerService(PlayerServiceClient client) {
		this.client = client;
	}

	public PlayerDTO getPlayer() {
		LOG.info("Call to getPlayer");
		return client.getPlayer();
	}

	public TransferResultDTO transfer(long amount) {
		LOG.info("Call to transfer, amount = {}", amount);
		return client.transfer(amount);
	}
}

package com.soze.common.client;

import com.soze.common.dto.PlayerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("player-service")
public interface PlayerServiceClient {

	@GetMapping(path = "/player", produces = MediaType.APPLICATION_JSON_VALUE)
	PlayerDTO getPlayer();

}

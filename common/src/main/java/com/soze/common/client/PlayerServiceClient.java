package com.soze.common.client;

import com.soze.common.dto.PlayerDTO;
import com.soze.common.dto.TransferResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("player-service")
public interface PlayerServiceClient {

	@GetMapping(path = "/player", produces = MediaType.APPLICATION_JSON_VALUE)
	PlayerDTO getPlayer();

	@PostMapping(path = "/transfer")
	TransferResultDTO transfer(@RequestParam("amount") long amount);

}

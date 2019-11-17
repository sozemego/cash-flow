package com.soze.truck.service;

import com.soze.common.dto.CityDTO;
import com.soze.truck.domain.Player;
import com.soze.truck.domain.Truck;
import com.soze.truck.external.RemoteWorldService;
import com.soze.truck.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TruckServiceStarter {

	private static final Logger LOG = LoggerFactory.getLogger(TruckServiceStarter.class);

	private final TruckService truckService;
	private final TruckTemplateLoader truckTemplateLoader;
	private final RemoteWorldService remoteWorldService;
	private final PlayerRepository playerRepository;

	@Autowired
	public TruckServiceStarter(TruckService truckService, TruckTemplateLoader truckTemplateLoader,
														 RemoteWorldService remoteWorldService, PlayerRepository playerRepository
														) {
		this.truckService = truckService;
		this.truckTemplateLoader = truckTemplateLoader;
		this.remoteWorldService = remoteWorldService;
		this.playerRepository = playerRepository;
	}

	public void createPlayer(UUID playerId) {
		LOG.info("createPlayer called for player = {}", playerId);
		Optional<Player> optionalPlayer = playerRepository.findById(playerId);
		if (optionalPlayer.isPresent()) {
			LOG.info("Player = {} already exists", playerId);
		} else {
			LOG.info("Player with id = {} does not exist yet, creating", playerId);
			Player player = new Player();
			player.setId(playerId);
			player.setInitialized(false);
			playerRepository.save(player);
		}
	}

	public void startPlayer(UUID playerId) {
		LOG.info("Starting player {}", playerId);
		Optional<Player> optionalPlayer = playerRepository.findById(playerId);
		if (!optionalPlayer.isPresent()) {
			LOG.info("Player = {} does not exist", playerId);
			return;
		}

		Player player = optionalPlayer.get();
		if (player.isInitialized()) {
			LOG.info("Player = {} is already initialized", playerId);
			return;
		}

		CityDTO city = remoteWorldService.getCityByName("Wroclaw");

		Truck truck1 = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		truck1.setPlayerId(playerId);
		truckService.addTruck(truck1, city.id);
		Truck truck2 = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		truck2.setPlayerId(playerId);
		truckService.addTruck(truck2, city.id);
		Truck truck3 = truckTemplateLoader.constructTruckByTemplateId("FAST_TRUCK");
		truck3.setPlayerId(playerId);
		truckService.addTruck(truck3, city.id);

		player.setInitialized(true);
		playerRepository.save(player);
	}

}

package com.soze.player;

import com.soze.player.controller.PlayerWebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

	private final PlayerWebSocketController playerWebSocketController;

	@Autowired
	public WebSocketConfiguration(PlayerWebSocketController playerWebSocketController) {
		this.playerWebSocketController = playerWebSocketController;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
		webSocketHandlerRegistry.addHandler(playerWebSocketController, "/websocket").setAllowedOrigins("*");
	}

}

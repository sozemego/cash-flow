package com.soze.factory;

import com.soze.factory.controller.FactoryWebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

	private final FactoryWebSocketController factoryWebSocketController;

	@Autowired
	public WebSocketConfiguration(FactoryWebSocketController factoryWebSocketController) {
		this.factoryWebSocketController = factoryWebSocketController;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
		webSocketHandlerRegistry.addHandler(factoryWebSocketController, "/websocket").setAllowedOrigins("*");
	}

}

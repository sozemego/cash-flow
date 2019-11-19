package com.soze.truck.ws;

import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

public class WebSocketFactory {

	public static WebSocket createSocket(WebSocketSession session, String playerName, UUID playerId) {
		return new WebSocketImpl(session, playerName, playerId);
	}

}

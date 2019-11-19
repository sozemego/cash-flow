package com.soze.truck.ws;

import com.soze.common.json.JsonUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class WebSocketImpl implements WebSocket {

	private final WebSocketSession session;
	private final String playerName;
	private final UUID playerId;

	public WebSocketImpl(WebSocketSession session, String playerName, UUID playerId) {
		Objects.requireNonNull(this.session = session);
		Objects.requireNonNull(this.playerName = playerName);
		Objects.requireNonNull(this.playerId = playerId);
	}

	@Override
	public String getId() {
		return session.getId();
	}

	@Override
	public String getPlayerName() {
		return playerName;
	}

	@Override
	public UUID getPlayerId() {
		return playerId;
	}

	@Override
	public void send(Object message) throws IOException {
		TextMessage textMessage = getTextMessage(message);
		session.sendMessage(textMessage);
	}

	private TextMessage getTextMessage(Object message) {
		if (message instanceof String) {
			return new TextMessage((String) message);
		}
		String payload = JsonUtils.serialize(message);
		return new TextMessage(payload);
	}

	@Override
	public void close() throws IOException {
		session.close();
	}


}

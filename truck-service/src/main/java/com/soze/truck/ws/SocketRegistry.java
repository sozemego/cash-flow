package com.soze.truck.ws;

import com.soze.common.json.JsonUtils;
import com.soze.common.message.server.ServerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SocketRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(SocketRegistry.class);

	private final Map<String, WebSocket> sockets = new ConcurrentHashMap<>();

	public void addSocket(WebSocket socket) {
		sockets.put(socket.getId(), socket);
	}

	public void removeSocket(WebSocket socket) {
		sockets.remove(socket.getId());
	}

	public void removeSession(WebSocketSession session) {
		sockets.remove(session.getId());
	}

	public WebSocket getWebSocket(WebSocketSession session) {
		return sockets.get(session.getId());
	}

	public void sendTo(WebSocket socket, ServerMessage serverMessage) {
		try {
			socket.send(serverMessage);
		} catch (IOException e) {
			LOG.warn("Exception when sending a server message, to session {}", socket.getId(), e);
		}
	}

	public void sendTo(WebSocket socket, String payload) {
		try {
			socket.send(payload);
		} catch (IOException e) {
			LOG.warn("Exception when sending a server message, to session {}", socket.getId(), e);
		}
	}

	public void sendToAll(ServerMessage serverMessage) {
		String payload = JsonUtils.serialize(serverMessage);
		for (WebSocket socket : sockets.values()) {
			sendTo(socket, payload);
		}
	}

}

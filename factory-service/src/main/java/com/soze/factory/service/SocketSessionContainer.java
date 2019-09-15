package com.soze.factory.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SocketSessionContainer {

	private static final Logger LOG = LoggerFactory.getLogger(SocketSessionContainer.class);

	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

	public void addSession(WebSocketSession session) {
		LOG.info("Adding session = {}", session.getId());
		sessions.put(session.getId(), session);
	}

	public void removeSession(String id) {
		LOG.info("Removing session = {}", id);
		sessions.remove(id);
	}

	public List<WebSocketSession> getAllSessions() {
		return new ArrayList<>(sessions.values());
	}

	public Optional<WebSocketSession> getSession(String id) {
		return Optional.ofNullable(sessions.get(id));
	}

}

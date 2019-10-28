package com.soze.factory.service;

import com.soze.common.json.JsonUtils;
import com.soze.common.message.server.ServerMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestWebSocketSession implements WebSocketSession {

	private final List<String> messages = new ArrayList<>();

	@Override
	public String getId() {
		return "session";
	}

	@Override
	public URI getUri() {
		return null;
	}

	@Override
	public HttpHeaders getHandshakeHeaders() {
		return null;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@Override
	public Principal getPrincipal() {
		return null;
	}

	@Override
	public InetSocketAddress getLocalAddress() {
		return null;
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return null;
	}

	@Override
	public String getAcceptedProtocol() {
		return null;
	}

	@Override
	public int getTextMessageSizeLimit() {
		return 0;
	}

	@Override
	public void setTextMessageSizeLimit(int messageSizeLimit) {

	}

	@Override
	public int getBinaryMessageSizeLimit() {
		return 0;
	}

	@Override
	public void setBinaryMessageSizeLimit(int messageSizeLimit) {

	}

	@Override
	public List<WebSocketExtension> getExtensions() {
		return null;
	}

	@Override
	public void sendMessage(WebSocketMessage<?> message) throws IOException {
		this.messages.add((String) message.getPayload());
	}

	@Override
	public boolean isOpen() {
		return false;
	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public void close(CloseStatus status) throws IOException {

	}

	public List<String> getMessages() {
		return messages;
	}

	public <T> T getMessage(int index, Class<T> clazz) {
		return JsonUtils.parse(messages.get(index), clazz);
	}
}

package com.soze.truck.ws;

import java.io.IOException;
import java.util.UUID;

/**
 * Socket connection to one client.
 */
public interface WebSocket {

	String getId();

	/**
	 * Name of the player that is connected with this socket.
	 */
	String getPlayerName();

	/**
	 * Id of the player connected with this socket.
	 */
	UUID getPlayerId();

	/**
	 * Sends a text message to the client.
	 */
	void send(Object message) throws IOException;

	void close() throws IOException;

}

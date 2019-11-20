package com.soze.common.message.queue;

public class PlayerCreated extends QueueMessage {

	public String userId;
	public String userName;
	public String playerId;
	public String playerName;

	public PlayerCreated() {

	}

	public PlayerCreated(String userId, String userName, String playerId, String playerName) {
		this.userId = userId;
		this.userName = userName;
		this.playerId = playerId;
		this.playerName = playerName;
	}

	@Override
	public QueueMessageType getType() {
		return QueueMessageType.PLAYER_CREATED;
	}
}

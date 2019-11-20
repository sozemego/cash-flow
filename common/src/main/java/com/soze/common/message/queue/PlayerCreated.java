package com.soze.common.message.queue;

public class PlayerCreated extends QueueMessage {

	public String userId;
	public String playerId;
	public String playerName;

	public PlayerCreated() {

	}

	public PlayerCreated(String userId, String playerId, String playerName) {
		this.userId = userId;
		this.playerId = playerId;
		this.playerName = playerName;
	}

	@Override
	public QueueMessageType getType() {
		return QueueMessageType.PLAYER_CREATED;
	}

	@Override
	public String toString() {
		return "PlayerCreated{" + "userId='" + userId + '\'' + ", playerId='" + playerId + '\'' + ", playerName='" + playerName + '\'' + '}';
	}
}

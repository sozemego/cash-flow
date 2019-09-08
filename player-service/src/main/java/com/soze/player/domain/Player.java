package com.soze.player.domain;

public class Player {

	private final String id;
	private final String name;
	private long cash;

	public Player(String id, String name, long cash) {
		this.id = id;
		this.name = name;
		this.cash = cash;
	}

	public Player(String id, String name) {
		this(id, name, 0);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getCash() {
		return cash;
	}

	public void setCash(long cash) {
		this.cash = cash;
	}
}

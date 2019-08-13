package com.soze.common.dto;

public class TileDTO {

	private int x;
	private int y;

	public TileDTO() {
	}

	public TileDTO(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}

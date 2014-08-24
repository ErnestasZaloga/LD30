package com.ld30.game.Model;

import com.ld30.game.Model.Tiles.Tile;

public class Blockade {

	private boolean active;
	private int cityIndex1;
	private int cityIndex2;
	private Tile tile;
	
	public int getCityIndex1() {
		return cityIndex1;
	}
	
	public void setCityIndex1(int cityIndex1) {
		this.cityIndex1 = cityIndex1;
	}
	
	public int getCityIndex2() {
		return cityIndex2;
	}
	
	public void setCityIndex2(int cityIndex2) {
		this.cityIndex2 = cityIndex2;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}
	
}

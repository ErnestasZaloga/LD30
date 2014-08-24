package com.ld30.game.Model;

import com.ld30.game.Model.Tiles.Tile;

public class Blockade {

	private boolean active;
	private Tile tile;
	
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

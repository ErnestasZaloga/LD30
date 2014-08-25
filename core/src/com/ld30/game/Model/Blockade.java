package com.ld30.game.Model;

import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.Model.moveable.BlockadeOrc;

public class Blockade {

	private BlockadeOrc owner;
	private boolean active;
	private Tile tile;
	
	public BlockadeOrc getOwner() {
		return owner;
	}

	public void setOwner(BlockadeOrc owner) {
		this.owner = owner;
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

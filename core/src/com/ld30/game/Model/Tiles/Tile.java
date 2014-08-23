package com.ld30.game.Model.Tiles;

import com.ld30.game.Model.Entity;

public abstract class Tile extends Entity {
	
	private boolean walkable;
	
	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}
	
	public boolean isWalkable() {
		return walkable;
	}
}

package com.ld30.game.Model.Tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ld30.game.Model.GameWorld;

public class Road extends Tile {
	
	private GameWorld.Center center;
	
	public Road(TextureRegion texture, float x, float y) {
		setX(x);
		setY(y);
		setTexture(texture);
		setWalkable(true);
	}
	
	public void setCenter(GameWorld.Center center) {
		this.center = center;
	}
	
	public GameWorld.Center getCenter() {
		return this.center;
	}
	
}

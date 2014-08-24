package com.ld30.game.Model.Tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ld30.game.Model.GameWorld;

public class Road extends Tile {
	
	private GameWorld.ResourceType center;
	
	public Road(TextureRegion texture, float x, float y) {
		setX(x);
		setY(y);
		setTexture(texture);
		setWalkable(true);
	}
	
	public void setCenter(GameWorld.ResourceType center) {
		this.center = center;
	}
	
	public GameWorld.ResourceType getCenter() {
		return this.center;
	}
	
}

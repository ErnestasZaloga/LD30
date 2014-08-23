package com.ld30.game.Model.Tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Water extends Tile {
	
	public Water(TextureRegion texture, float x, float y) {
		setX(x);
		setY(y);
		setTexture(texture);
		setWalkable(false);
	}

}

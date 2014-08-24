package com.ld30.game.Model.Tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Rock extends Tile {

	public Rock(TextureRegion texture, float x, float y) {
		setX(x);
		setY(y);
		setTexture(texture);
	}
	
}

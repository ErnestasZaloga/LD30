package com.ld30.game.Model.Tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tree extends Tile {

	public Tree(TextureRegion texture, float x, float y) {
		super(0f);
		
		setX(x);
		setY(y);
		setTexture(texture);
	}
	
}

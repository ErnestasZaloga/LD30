package com.ld30.game.Model.Tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Grass extends Tile {
	
	public Grass(TextureRegion texture, float x, float y) {
		super(1f);
		
		setX(x);
		setY(y);
		setTexture(texture);
		setDarkenPercent(MathUtils.random(0f, 0.15f));
	}
	
}

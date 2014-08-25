package com.ld30.game.Model.Tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class ShallowWater extends Tile {

	public ShallowWater(TextureRegion texture, float x, float y) {
		super(0f);
		
		setX(x);
		setY(y);
		setTexture(texture);
	}

	public void update (final float delta) {
		super.update(delta);
		setDarkenPercent(getPercent() * 0.15f);
	}
	
}

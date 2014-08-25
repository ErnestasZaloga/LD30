package com.ld30.game.Model.Tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.ld30.game.Model.GameWorld;

public class Road extends Tile {
	
	private GameWorld.ResourceType type;
	private float originalDarken;
	private boolean animating;
	
	public Road(TextureRegion texture, float x, float y) {
		super(0.5f);
		
		setX(x);
		setY(y);
		setTexture(texture);
		setDarkenPercent(MathUtils.random(0f, 0.15f));
		
		originalDarken = getDarkenPercent();
	}
	
	public void startAnimation () {
		animating = true;
	}
	
	public void stopAnimation () {
		animating = false;
		setDarkenPercent(originalDarken);
	}
	
	public float getOriginalDarken() {
		return originalDarken;
	}

	public void setType(GameWorld.ResourceType type) {
		this.type = type;
	}
	
	public GameWorld.ResourceType getType() {
		return this.type;
	}
	
	@Override
	public void hit () {
	}
	
	@Override
	public void update(final float delta) {
		if (animating) {
			super.update(delta);
		}
	}
	
}

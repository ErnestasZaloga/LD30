package com.ld30.game.Model.Tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.ld30.game.Model.GameWorld;

public class Road extends Tile {
	
	private GameWorld.ResourceType type;
	private boolean waiting;
	private float originalDarken;
	
	public Road(TextureRegion texture, float x, float y) {
		super(0.5f);
		
		setX(x);
		setY(y);
		setTexture(texture);
		setDarkenPercent(MathUtils.random(0f, 0.15f));
		setTemporalAnimation(true);
		
		originalDarken = getDarkenPercent();
	}
	
	public void setType(GameWorld.ResourceType type) {
		this.type = type;
	}
	
	public GameWorld.ResourceType getType() {
		return this.type;
	}
	
	@Override
	public void hit () {
		super.hit();
		waiting = true;
	}
	
	@Override
	public void update(final float delta) {
		if (waiting) {
			super.update(delta);
			final float percent = getPercent();
			setDarkenPercent(percent * 0.15f + originalDarken);
			
			if (percent == 0f) {
				waiting = false;
			}
		}
	}
	
}

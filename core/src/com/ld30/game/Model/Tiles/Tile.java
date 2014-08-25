package com.ld30.game.Model.Tiles;

import com.badlogic.gdx.math.MathUtils;
import com.ld30.game.Model.Entity;

public abstract class Tile extends Entity {
	private static final float SPEED = 2f;
	private static final float HALF_SPEED = SPEED / 2f;
	
	private float darkenPercent = MathUtils.random(0f, 0.15f);

	private float stateTime;
	private float hitPosition;
	private boolean temporalAnimation = false;
	
	protected Tile (final float hitPosition) {
		this.hitPosition = hitPosition;
		stateTime = MathUtils.random(0f, SPEED);
	}
	
	protected void setTemporalAnimation (final boolean temporalAnimation) {
		this.temporalAnimation = temporalAnimation;
	}
	
	public float getDarkenPercent() {
		return darkenPercent;
	}

	public void setDarkenPercent(float darkenPercent) {
		this.darkenPercent = darkenPercent;
	}
	
	public void update(final float delta) {
		stateTime += delta;
		
		if (stateTime > SPEED) {
			if (temporalAnimation) {
				stateTime = SPEED;
			}
			else {
				stateTime = 0f;
			}
		}
	}
	
	public void hit () {
		stateTime = 2f * hitPosition;
	}
	
	protected float getPercent () {
		if (stateTime > HALF_SPEED) {
			return (HALF_SPEED - (stateTime - HALF_SPEED)) / HALF_SPEED;
		}
		else {
			return stateTime / HALF_SPEED;
		}
	}
}

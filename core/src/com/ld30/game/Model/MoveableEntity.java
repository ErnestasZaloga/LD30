package com.ld30.game.Model;

public abstract class MoveableEntity extends Entity {

	private float stateTime;
	private float pixelsPerSecond;
	
	public void setStateTime (final float stateTime) {
		this.stateTime = stateTime;
	}
	
	public float getStateTime () {
		return stateTime;
	}
	
	public float getPixelsPerSecond() {
		return pixelsPerSecond;
	}

	public void setPixelsPerSecond(float pixelsPerSecond) {
		this.pixelsPerSecond = pixelsPerSecond;
	}
	
}


package com.ld30.game.Model;

public abstract class MoveableEntity extends Entity {

	private float pixelsPerSecond;
	private float stateTime;
	
	public float getStateTime() {
		return stateTime;
	}

	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	public float getPixelsPerSecond() {
		return pixelsPerSecond;
	}

	public void setPixelsPerSecond(float pixelsPerSecond) {
		this.pixelsPerSecond = pixelsPerSecond;
	}
	
}


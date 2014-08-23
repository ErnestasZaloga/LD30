package com.ld30.game.Model;

public abstract class MoveableEntity extends Entity {

	private float pixelsPerSecond;

	public float getPixelsPerSecond() {
		return pixelsPerSecond;
	}

	public void setPixelsPerSecond(float pixelsPerSecond) {
		this.pixelsPerSecond = pixelsPerSecond;
	}
	
}


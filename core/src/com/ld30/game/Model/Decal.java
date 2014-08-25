package com.ld30.game.Model;

public class Decal extends Entity {

	private float stateTime;
	private float speed;
	
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public boolean update (final float delta) {
		stateTime += delta;
		
		if (stateTime > speed) {
			return true;
		}
		
		return false;
	}
	
	public float getPercent () {
		return stateTime / speed;
	}
	
}

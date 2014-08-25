package com.ld30.game.Model.moveable;

import com.badlogic.gdx.math.MathUtils;
import com.ld30.game.Model.City;
import com.ld30.game.Model.Decal;
import com.ld30.game.Model.GameWorld;

public class BruteOrc extends Orc {

	private float attackStateTime = MathUtils.random(0f, 1f);
	private City target;
	private Troop secondaryTarget;
	private float health = 0.33f;

	public Troop getSecondaryTarget() {
		return secondaryTarget;
	}

	public void setSecondaryTarget(Troop secondaryTarget) {
		this.secondaryTarget = secondaryTarget;
	}

	public City getTarget() {
		return target;
	}

	public void setTarget(City target) {
		this.target = target;
	}
	
	public void update (final float delta, GameWorld gameWorld) {
		attackStateTime += delta;
		
		if (attackStateTime >= 1f) {
			if (secondaryTarget != null) {
				secondaryTarget.hit(gameWorld);
			}
			else {
				target.hit(this);
			}
			attackStateTime = 0f;
		}
	}
	
	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	public void hit (final GameWorld gameWorld) {
		final Decal decal = new Decal();
		decal.setTexture(gameWorld.getAssets().hit);
		decal.setWidth(gameWorld.getMap().getTileWidth());
		decal.setHeight(gameWorld.getMap().getTileHeight());
		decal.setX(getX());
		decal.setY(getY());
		decal.setSpeed(0.3f);
		gameWorld.getDecals().add(decal);
		
		health -= 0.33f;
	}
	
}

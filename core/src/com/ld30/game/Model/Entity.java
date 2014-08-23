package com.ld30.game.Model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


public abstract class Entity {
	
	private TextureRegion texture;
	private float x, y;
	
	public Entity() {
		
	}

	public TextureRegion getTexture() {
		return texture;
	}

	public void setTexture(TextureRegion texture) {
		this.texture = texture;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
}

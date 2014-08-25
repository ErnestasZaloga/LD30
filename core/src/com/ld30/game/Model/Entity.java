package com.ld30.game.Model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


public abstract class Entity {
	
	private TextureRegion texture;
	private float x, y;
	private float width, height;
	
	public Entity() {
		
	}

	public TextureRegion getTexture() {
		return texture;
	}

	public void setTexture(TextureRegion texture) {
		this.texture = texture;
		if (width == 0) {
			width = texture.getRegionWidth();
		}
		if (height == 0) {
			height = texture.getRegionHeight();
		}
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
	
	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void scale (final float scale) {
		width = width * scale;
		height = height * scale;
	}
}

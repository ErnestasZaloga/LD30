package com.ld30.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	
	public final Skin UISkin;

	private final Texture tileTexture;
	private final Texture cityTexture;
	private final Texture moveableTexture;
	private final Texture waterTexture;
	private final Texture roadTexture;
	
	public final TextureRegion grass;
	public final TextureRegion water;
	public final TextureRegion road;
	public final TextureRegion moveable;
	public final TextureRegion city;
	
	private final int tileWH = 8;
	
	public Assets () {
		UISkin = new Skin(Gdx.files.internal("UISkin/uiskin.json"));
		
		Pixmap pixmap = new Pixmap(tileWH, tileWH, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fillRectangle(0, 0, tileWH, tileWH);
		
		tileTexture = new Texture(pixmap);
		pixmap.dispose();
		
		grass = new TextureRegion(tileTexture);
		
		pixmap = new Pixmap(tileWH * 10, tileWH * 10, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
		
		cityTexture = new Texture(pixmap);
		pixmap.dispose();
		city = new TextureRegion(cityTexture);
		
		pixmap = new Pixmap(tileWH, tileWH, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.RED);
		pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
		
		moveableTexture = new Texture(pixmap);
		pixmap.dispose();
		moveable = new TextureRegion(moveableTexture);
		
		pixmap = new Pixmap(tileWH, tileWH, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.BLUE);
		pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
		
		waterTexture = new Texture(pixmap);
		pixmap.dispose();
		water = new TextureRegion(waterTexture);
		
		pixmap = new Pixmap(tileWH, tileWH, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.YELLOW);
		pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
		
		roadTexture = new Texture(pixmap);
		pixmap.dispose();
		road = new TextureRegion(roadTexture);
	}
	
	public void dispose () {
		tileTexture.dispose();
		cityTexture.dispose();
		moveableTexture.dispose();
	}
	
}

package com.ld30.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	
	public final Skin UISkin;

	/*private final Texture tileTexture;
	private final Texture cityTexture;
	private final Texture moveableTexture;
	private final Texture waterTexture;
	private final Texture shallowWaterTexture;
	private final Texture roadTexture;
	private final Texture rockTexture;
	private final Texture treeTexture;
	private final Texture blockadeTexture;
	private final Texture soldierTexture;*/
	private final TextureAtlas atlas;
	
	public final TextureRegion blockade;
	public final TextureRegion grass;
	public final TextureRegion water;
	public final TextureRegion shallowWater;
	public final TextureRegion rock;
	public final TextureRegion tree;
	public final TextureRegion road;
	public final TextureRegion moveable;
	public final TextureRegion city;
	public final TextureRegion black;
	public final TextureRegion soldier;
	
	private final int tileWH = 8;
	
	public Assets () {
		UISkin = new Skin(Gdx.files.internal("UISkin/uiskin.json"));
		
		atlas = new TextureAtlas(Gdx.files.internal("textures/xhdpi/atlas/atlas.pack"));
		
		/*Pixmap pixmap = new Pixmap(tileWH, tileWH, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fillRectangle(0, 0, tileWH, tileWH);
		
		tileTexture = new Texture(pixmap);
		pixmap.dispose();*/
		
		grass = new TextureRegion(atlas.findRegion("Zole"));
		
		/*pixmap = new Pixmap(tileWH * 10, tileWH * 10, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fillRectangle(0, 0, tileWH * 10, tileWH * 10);
		
		cityTexture = new Texture(pixmap);
		pixmap.dispose();*/
		city = new TextureRegion(atlas.findRegion("PilisMedine"));
		
		/*pixmap = new Pixmap(tileWH, tileWH, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.RED);
		pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
		
		moveableTexture = new Texture(pixmap);
		pixmap.dispose();*/
		moveable = new TextureRegion(atlas.findRegion("Darbininkas"));
		
		/*pixmap = new Pixmap(tileWH, tileWH, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.BLUE);
		pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
		
		waterTexture = new Texture(pixmap);
		pixmap.dispose();*/
		water = new TextureRegion(atlas.findRegion("VanduoTamsus"));
		
		/*pixmap = new Pixmap(tileWH, tileWH, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.CYAN);
		pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
		
		shallowWaterTexture = new Texture(pixmap);
		pixmap.dispose();*/
		shallowWater = new TextureRegion(atlas.findRegion("VanduoSviesus"));
		
		/*pixmap = new Pixmap(tileWH, tileWH, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.YELLOW);
		pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
		
		roadTexture = new Texture(pixmap);
		pixmap.dispose();*/
		road = new TextureRegion(atlas.findRegion("Kelias"));
		
		/*pixmap = new Pixmap(tileWH, tileWH, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.GRAY);
		pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
		
		rockTexture = new Texture(pixmap);
		pixmap.dispose();*/
		rock = new TextureRegion(atlas.findRegion("Akmuo"));
		
		/*pixmap = new Pixmap(tileWH, tileWH, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.TEAL);
		pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
		
		treeTexture = new Texture(pixmap);
		pixmap.dispose();*/
		tree = new TextureRegion(atlas.findRegion("Medis"));
		
		/*pixmap = new Pixmap(tileWH, tileWH, Pixmap.Format.RGBA8888);
		pixmap.setColor(new Color(0, 0, 0, 0.5f));
		pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
		*/
		black = new TextureRegion(atlas.findRegion("Black"));
		/*pixmap.dispose();
		
		pixmap = new Pixmap(tileWH, tileWH, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.BLACK);
		blockadeTexture = new Texture(pixmap);
		pixmap.dispose();*/
		blockade = new TextureRegion(atlas.findRegion("Black"));
		
		/*pixmap = new Pixmap(tileWH, tileWH, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.PINK);
		pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
		soldierTexture = new Texture(pixmap);
		pixmap.dispose();*/
		soldier = new TextureRegion(atlas.findRegion("Karys"));
	}
	
	public void dispose () {
		atlas.dispose();
	}
	
}

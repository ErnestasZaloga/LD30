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
	
	public final TextureRegion foodIcon;
	public final TextureRegion metalIcon;
	public final TextureRegion woodIcon;
	public final TextureRegion soldierIcon;
	public final TextureRegion workerIcon;
	
	private final int tileWH = 8;
	
	public Assets () {
		UISkin = new Skin(Gdx.files.internal("UISkin/uiskin.json"));
		
		atlas = new TextureAtlas(Gdx.files.internal("textures/xhdpi/atlas/atlas.pack"));
		
		
		//game graphycs
		grass = new TextureRegion(atlas.findRegion("Zole"));
		city = new TextureRegion(atlas.findRegion("PilisMedine"));
		moveable = new TextureRegion(atlas.findRegion("Darbininkas"));
		water = new TextureRegion(atlas.findRegion("VanduoTamsus"));
		shallowWater = new TextureRegion(atlas.findRegion("VanduoSviesus"));
		road = new TextureRegion(atlas.findRegion("Kelias"));
		rock = new TextureRegion(atlas.findRegion("Akmuo"));
		tree = new TextureRegion(atlas.findRegion("Medis"));
		black = new TextureRegion(atlas.findRegion("Black"));
		blockade = new TextureRegion(atlas.findRegion("Black"));
		soldier = new TextureRegion(atlas.findRegion("Karys"));
		
		//game resource ui icons
		foodIcon = new TextureRegion(atlas.findRegion("MaistasIkona"));
		metalIcon = new TextureRegion(atlas.findRegion("GelezisIkona"));
		woodIcon = new TextureRegion(atlas.findRegion("MedisIkona"));
		soldierIcon = new TextureRegion(atlas.findRegion("KarysIkona"));
		workerIcon = new TextureRegion(atlas.findRegion("DarbininkasIkona"));
		
	}
	
	public void dispose () {
		atlas.dispose();
	}
	
}

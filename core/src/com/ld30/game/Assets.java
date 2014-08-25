package com.ld30.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	
	public final Skin UISkin;

	private final TextureAtlas atlas;
	
	public final TextureRegion blockade;
	public final TextureRegion grass;
	public final TextureRegion water;
	public final TextureRegion shallowWater;
	public final TextureRegion rock;
	public final TextureRegion tree;
	public final TextureRegion road;
	public final TextureRegion worker;
	public final TextureRegion bruteOrc;
	public final TextureRegion blockadeOrc;
	public final TextureRegion troop;
	public final TextureRegion ironCity;
	public final TextureRegion foodCity;
	public final TextureRegion woodCity;
	public final TextureRegion black;
	public final TextureRegion hit;
	public final TextureRegion blood;
	
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
		grass = atlas.findRegion("Zole");
		woodCity = atlas.findRegion("PilisMedine");
		ironCity = atlas.findRegion("PilisAkmenine");
		foodCity = atlas.findRegion("PilisGyvuliu");
		worker = atlas.findRegion("Darbininkas");
		water = atlas.findRegion("VanduoTamsus");
		shallowWater = atlas.findRegion("VanduoSviesus");
		road = atlas.findRegion("Kelias");
		rock = atlas.findRegion("Akmuo");
		tree = atlas.findRegion("Medis");
		black = atlas.findRegion("Black");
		blockade = atlas.findRegion("OrkasBarikada");
		troop = atlas.findRegion("Karys");
		bruteOrc = atlas.findRegion("Orkas");
		blockadeOrc = atlas.findRegion("OrkasNesasi");
		hit = atlas.findRegion("Trenkia");
		blood = atlas.findRegion("Kraujas");
		
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

package com.ld30.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
	
	public final Music music;
	
	public final Sound sirena;
	public final Sound troopDead;
	public final Sound resourcesLost;
	public final Sound blipSelect;
	public final Sound click;
	public final Sound cityDestroyed;
	public final Sound workerPathFinished;
	public final Sound troopPathFinished;
	public final Sound resourcePathFinished;
	
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
		
		music = Gdx.audio.newMusic(Gdx.files.internal("music/Music.mp3"));
		music.play();
		music.setLooping(true);
		
		sirena = Gdx.audio.newSound(Gdx.files.internal("sounds/8bit sirenos.wav"));
		troopDead = Gdx.audio.newSound(Gdx.files.internal("sounds/atimimo1.wav"));
		resourcesLost = Gdx.audio.newSound(Gdx.files.internal("sounds/atimimo3.wav"));
		blipSelect = Gdx.audio.newSound(Gdx.files.internal("sounds/Blip_Select10.wav"));
		click = Gdx.audio.newSound(Gdx.files.internal("sounds/click5.wav"));
		cityDestroyed = Gdx.audio.newSound(Gdx.files.internal("sounds/sugriuvimas2.wav"));
		workerPathFinished = Gdx.audio.newSound(Gdx.files.internal("sounds/unituIejimas1.wav"));
		troopPathFinished = Gdx.audio.newSound(Gdx.files.internal("sounds/unituIejimas2.wav"));
		resourcePathFinished = Gdx.audio.newSound(Gdx.files.internal("sounds/unituIejimas3.wav"));
	}
	
	public void dispose () {
		atlas.dispose();
	}
	
}

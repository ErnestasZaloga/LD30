package com.ld30.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ld30.game.Screens.GameScreen;

public class LD30 extends Game {
	
	private SpriteBatch batch;
	private Assets assets;
	private GameScreen gameScreen;
	
	@Override
	public void create () {
		Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode());
		batch = new SpriteBatch(1000);
		assets = new Assets();

		gameScreen = new GameScreen(batch, assets);
		setScreen(gameScreen);
		//Vyto pushas
		//Pauliaus pushas
	}

	@Override
	public void render () {
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		assets.dispose();
		batch.dispose();
	}
}

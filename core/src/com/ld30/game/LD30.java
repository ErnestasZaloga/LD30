package com.ld30.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ld30.game.Screens.MainMenuScreen;

public class LD30 extends Game {
	
	private SpriteBatch batch;
	private Assets assets;
	
	public SpriteBatch getBatch() {
		return batch;
	}

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	public Assets getAssets() {
		return assets;
	}

	public void setAssets(Assets assets) {
		this.assets = assets;
	}

	@Override
	public void create () {
		Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode());
		batch = new SpriteBatch(1000);
		assets = new Assets();

		setScreen(new MainMenuScreen(this, batch, assets));
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

package com.ld30.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LD30 extends ApplicationAdapter {
	
	private SpriteBatch batch;
	
	@Override
	public void create () {
		Gdx.graphics.setDisplayMode(Gdx.graphics.getDisplayModes()[0]);
		batch = new SpriteBatch(1000);
	}

	@Override
	public void render () {
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
	} 
}

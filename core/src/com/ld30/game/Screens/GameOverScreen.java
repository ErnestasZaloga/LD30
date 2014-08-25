package com.ld30.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ld30.game.Assets;
import com.ld30.game.LD30;

public class GameOverScreen implements Screen {
	private LD30 game;
	private Assets assets;
	private long time;
	
	private SpriteBatch batch;
	private Stage stage;
	private Skin skin;
	
	public GameOverScreen(LD30 game, SpriteBatch batch, Assets assets, long time) {
		this.game = game;
		this.batch = batch;
		this.assets = assets;
		this.time = time;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("UISkin/uiskin.json"));
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

}

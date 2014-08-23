package com.ld30.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ld30.game.Assets;
import com.ld30.game.Model.GameWorld;
import com.ld30.game.View.WorldRenderer;


public class GameScreen implements Screen {
	
	SpriteBatch batch;
	GameWorld gameWorld;
	WorldRenderer renderer;
	
	public GameScreen(SpriteBatch batch, Assets assets) {
		this.batch = batch;
		gameWorld = new GameWorld(assets);
		renderer = new WorldRenderer(batch, gameWorld);
	}

	@Override
	public void render(float delta) {
		gameWorld.update(Gdx.graphics.getDeltaTime());
		renderer.render();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		gameWorld.begin();
	}

	@Override
	public void hide() {
		// TODO: gameWorld end
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}

}

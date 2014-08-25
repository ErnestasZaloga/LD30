package com.ld30.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ld30.game.Assets;
import com.ld30.game.LD30;

public class MainMenuScreen implements Screen {
	private LD30 game;
	private Assets assets;
	
	private SpriteBatch batch;
	private Stage stage;
	private Skin skin;
	private Skin resources;
	private TextureAtlas atlas;
	
	private Table leftTable = new Table();
	private Table midTable = new Table();
	private Table rightTable = new Table();
	private TextButton playBtn;
	
	public MainMenuScreen(LD30 game, SpriteBatch batch, Assets assets) {
		this.game = game;
		this.batch = batch;
		this.assets = assets;
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
		atlas = new TextureAtlas(Gdx.files.internal("textures/xhdpi/atlas/atlas.pack"));
		resources = new Skin(atlas);
		Gdx.input.setInputProcessor(stage);
				
		stage.addActor(leftTable);
		leftTable.setWidth(Gdx.graphics.getWidth()/3);
		leftTable.setHeight(Gdx.graphics.getHeight());
		stage.addActor(midTable);
		midTable.setWidth(Gdx.graphics.getWidth()/3);
		midTable.setX(leftTable.getX()+leftTable.getWidth());
		midTable.setHeight(Gdx.graphics.getHeight());
		stage.addActor(rightTable);
		rightTable.setWidth(Gdx.graphics.getWidth()/3);
		rightTable.setHeight(Gdx.graphics.getHeight());
		rightTable.setX(midTable.getX()+midTable.getWidth());
		
		/*
		 * Left table
		 */
		leftTable.add(new Image(resources.getDrawable("PilisGyvuliu")));
		leftTable.add(new Label(" - This is a castle of animal herders.\nThey provide food.", skin));
		leftTable.row();
		
		leftTable.add(new Image(resources.getDrawable("PilisAkmenine")));
		leftTable.add(new Label(" - This is a castle of miners.\nThey provide metals.", skin));
		leftTable.row();
		
		leftTable.add(new Image(resources.getDrawable("PilisMedine")));
		leftTable.add(new Label(" - This is a castle of woodcutters.\nThey provide wood.", skin));
		leftTable.row();
		
		/*
		 * Middle table
		 */
		playBtn = new TextButton("Play", skin);
		playBtn.setX(midTable.getWidth()/2-playBtn.getWidth()/2);
		playBtn.setY(midTable.getHeight()/2+playBtn.getHeight()/2);
		playBtn.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new GameScreen(batch, assets));
			}
		});
		midTable.addActor(playBtn);
		
		/*
		 * Right table
		 */
		//TODO
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

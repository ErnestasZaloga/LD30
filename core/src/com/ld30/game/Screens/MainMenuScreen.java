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
		leftTable.setWidth(Gdx.graphics.getWidth()/100*40);
		leftTable.setHeight(Gdx.graphics.getHeight());
		stage.addActor(midTable);
		midTable.setWidth(Gdx.graphics.getWidth()/100*20);
		midTable.setX(leftTable.getX()+leftTable.getWidth());
		midTable.setHeight(Gdx.graphics.getHeight());
		stage.addActor(rightTable);
		rightTable.setWidth(Gdx.graphics.getWidth()/100*40);
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
		
		leftTable.add(new Image(resources.getDrawable("Darbininkas")));
		leftTable.add(new Label(" - This is a worker. He will work in the city he is in,\nthus generating resources\nof the same type the city is."
				+ "\nWorker costs 2 units of each resource\n"
				+ "and can carry 2 units of single type resource.\n"
				+ "Type depends on workers current city.", skin));
		leftTable.row();
		
		leftTable.add(new Image(resources.getDrawable("Karys")));
		leftTable.add(new Label(" - This is a soldier. He will defend the city\nin which he currently is."
				+ "\nSoldier costs 4 units of each resource.", skin));
		leftTable.row();
		
		leftTable.add(new Image(resources.getDrawable("Orkas")));
		leftTable.add(new Label(" - This is an orc. Packs of them come to\nkill people of your cities.", skin));
		leftTable.row();
		
		leftTable.add(new Image(resources.getDrawable("OrkasBarikada")));
		leftTable.add(new Label(" - This is an orc which will barricade the road.\nIf worker is carrying resources and hits the barricade,\nhe will loose all resources.", skin));
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
		Label l = new Label("", skin);
		//l.setWidth(rightTable.getWidth());
		
		l.setText("You play as a ruler of three diferent cities.\n"
				+ "All those cities are connected with\n"
				+ "roads, through which your\nsoldiers and workers can pass.\n"
				+ "Each city generates one of the following resources:\n"
				+ "food, wood and iron. But the city does so\nonly if it has "
				+ "at least one worker residing in it.\n"
				+ "The goal of the game is to survive\n"
				+ "continuous orc raids for as long as you can\n"
				+ "by managing your human and non-human\n"
				+ "resources wisely. Orcs either attack\n"
				+ "your city directly or build barricades.\n"
				+ "If orc attacks a city, it first fights\n"
				+ "residing soldiers and if there are none\n"
				+ "he then looks for workers (which do not\n"
				+ "fight back). If orc builds a barricade,\n"
				+ "workers will not be able to pass through\n"
				+ "and have their resources stolen.\n"
				+ "In order to break the barricade down,\n"
				+ "you should send a troop to it.");
		rightTable.add(l);
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

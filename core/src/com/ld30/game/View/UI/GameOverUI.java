package com.ld30.game.View.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.ld30.game.Assets;
import com.ld30.game.LD30;
import com.ld30.game.Model.City;
import com.ld30.game.Screens.MainMenuScreen;

public class GameOverUI extends Group{
	private final GameUI gameUI;
	private final Assets assets;
	
	private final Image background;
	private final Label gameOver;
	private final Skin skin;
	private final TextButton replayButton;
	
	Label survivalTime;
	
	public GameOverUI(final GameUI gameUI, Assets assets, Array<City> cities){
		this.gameUI = gameUI;
		this.assets = assets;
		skin = assets.UISkin;
		
		background = new Image(assets.black);
		addActor(background);
		
		gameOver = new Label("GAME OVER", skin);
		gameOver.setFontScale(5);
		gameOver.getColor().r = 1f;
		gameOver.getColor().g = 1f;
		gameOver.getColor().b = 1f;
		gameOver.pack();
		addActor(gameOver);
		
		replayButton = new TextButton("REPLAY", skin);
		replayButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				LD30 game = ((LD30) Gdx.app.getApplicationListener());
				game.setScreen(new MainMenuScreen(game, game.getBatch(), game.getAssets()));
				return true;
			}
		});
		addActor(replayButton);
	}
	
	public void update() {
		survivalTime = new Label ("SURVIVAL TIME: " + gameUI.getTopUI().getTimer().getFormatedTime(), assets.UISkin);
		survivalTime.setColor(Color.YELLOW);
		survivalTime.setFontScale(3.5f);
		survivalTime.pack();
		addActor(survivalTime);
		
		
		setSize(getWidth(), getHeight());
	}
	
	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
		
		background.setSize(width, height);
		gameOver.setPosition((width - gameOver.getWidth()) / 2, (height - gameOver.getHeight()) / 2 + height / 4);
		
		if (survivalTime != null) {
			survivalTime.setPosition(width / 2f - survivalTime.getWidth() / 2f, gameOver.getY() - survivalTime.getHeight());
			replayButton.setPosition(width / 2f - replayButton.getWidth() / 2f, survivalTime.getY() - replayButton.getHeight() * 2f);
		}
	}

}

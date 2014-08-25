package com.ld30.game.View.UI;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.ld30.game.Assets;
import com.ld30.game.Model.City;

public class GameOverUI extends Group{
	private final Image background;
	private final Label gameOver;
	
	private final Skin skin;
	
	private int score;
	
	public GameOverUI(Assets assets, Array<City> cities){
		skin = assets.UISkin;
		
		background = new Image(assets.black);
		addActor(background);
		
		
		gameOver = new Label("GAME OVER", skin);
		gameOver.setFontScale(5);
		gameOver.getColor().r = 0.0f;
		gameOver.getColor().g = 0.0f;
		gameOver.getColor().b = 0.0f;
		gameOver.pack();
		addActor(gameOver);
	}
	
	public void update() {
		
	}
	
	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
		
		background.setSize(width, height);
		gameOver.setPosition((width - gameOver.getWidth()) / 2, (height - gameOver.getHeight()) / 2 + height / 4);
		
	}

}

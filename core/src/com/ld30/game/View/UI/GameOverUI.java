package com.ld30.game.View.UI;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GameOverUI extends Group{
	private final Image gameOver;
	private final Image background;
	
	private int score;
	
	public GameOverUI(){
		background = new Image();
		
		gameOver = new Image();
		
	}
	
	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
		
		background.setSize(width, height);
		
	}

}

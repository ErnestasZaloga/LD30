package com.ld30.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.ld30.game.Assets;
import com.ld30.game.Model.City;
import com.ld30.game.Model.GameWorld;

public class GameUI {
	private SpriteBatch batch;
	private final Stage stage;
	private final GameWorld gameWorld;
	
	private Array<CityUI> cityGroups;
	
	private final float width;
	private final float height;
	
	private BitmapFont font;
	
	public GameUI(final GameWorld gameWorld, final Array<City> cities, final Assets assets) {
		this.gameWorld = gameWorld;
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		stage = new Stage(new StretchViewport(width, height));
		
		cityGroups = new Array<CityUI>(cities.size);
		for(int i = 0, n = cities.size; i < n; i++) {
			CityUI group = new CityUI(cities.get(i), font, assets);
			
			cityGroups.add(group);
			stage.addActor(group);
			
			
		}
		
	}
	
	public void updateAndRender(SpriteBatch batch) {
		
		
		
		
		stage.act();
		batch.begin();
		stage.draw();
		batch.end();
	}
	
	private class CityUI extends Group {
		
		private final City city;
		
		private final Image UIBackground;
		
		private final Image food;
		private final Image metal;
		private final Image wood;
		
		private final Label foodNumber;
		private final Label metalNumber;
		private final Label woodNumber;
		private final Skin skin;
		
		public CityUI(final City city, final BitmapFont font, final Assets assets) {
			this.city = city;
			skin = assets.UISkin;
			
			UIBackground = new Image();
			
			food = new Image();
			metal = new Image();
			wood = new Image();
			
			foodNumber = new Label("", skin);
			metalNumber = new Label("", skin);
			woodNumber = new Label("", skin);
		}
	}
}

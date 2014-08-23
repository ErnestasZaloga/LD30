package com.ld30.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
	
	private Array<CityUI> cityUIs;
	
	private final float width;
	private final float height;
	
	private BitmapFont font;
	
	public GameUI(final GameWorld gameWorld, final Array<City> cities, final Assets assets) {
		this.gameWorld = gameWorld;
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		stage = new Stage(new StretchViewport(width, height));
		
		cityUIs = new Array<CityUI>(cities.size);
		for(int i = 0, n = cities.size; i < n; i++) {
			City city = cities.get(i);
			CityUI group = new CityUI(city, assets);
			
			group.setSize(city.getWidth(), 32); //FIXME hardcode
			cityUIs.add(group);
			stage.addActor(group);
			
			
		}
		
	}
	
	public void updateAndRender(SpriteBatch batch) {
		
		//Update
		for(CityUI ui : cityUIs) {
			ui.update();
			City city = ui.city;
			ui.setPosition(city.getX(), city.getY()); //TODO improve...
		}
		
		//Render
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
		private final Image soldiers;
		private final Image workers;
		
		private final Image[] icons;
		
		private final Label foodNumber;
		private final Label metalNumber;
		private final Label woodNumber;
		private final Label soldierCount;
		private final Label workerCount;
		private final Skin skin;
		
		public CityUI(final City city, final Assets assets) {
			this.city = city;
			skin = assets.UISkin;
			
			UIBackground = new Image();
			addActor(UIBackground);
			
			food = new Image();
			metal = new Image();
			wood = new Image();
			soldiers = new Image();
			workers = new Image();
			icons = new Image[5];
			icons[0] = food;
			icons[1] = food;
			icons[2] = food;
			icons[3] = food;
			icons[4] = food;
			for(int i = 0; i < 5; i++) {
				addActor(icons[i]);
			}
			
			foodNumber = new Label("", skin);
			metalNumber = new Label("", skin);
			woodNumber = new Label("", skin);
			soldierCount = new Label("", skin);
			workerCount = new Label("", skin);
			addActor(foodNumber);
			addActor(metalNumber);
			addActor(woodNumber);
			addActor(soldierCount);
			addActor(workerCount);
		}
		
		public void update() {
			foodNumber.setText(String.valueOf(city.getFood()));
			metalNumber.setText(String.valueOf(city.getMetal()));
			woodNumber.setText(String.valueOf(city.getWood()));
			soldierCount.setText(String.valueOf(city.getSoldierCount()));
			workerCount.setText(String.valueOf(city.getWorkerCount()));
			
			foodNumber.setPosition(food.getX() + (food.getWidth() - foodNumber.getWidth()) / 2, 0);
			metalNumber.setPosition(food.getX() + (food.getWidth() - foodNumber.getWidth()) / 2, 0);
			woodNumber.setPosition(food.getX() + (food.getWidth() - foodNumber.getWidth()) / 2, 0);
			soldierCount.setPosition(food.getX() + (food.getWidth() - foodNumber.getWidth()) / 2, 0);
			workerCount.setPosition(food.getX() + (food.getWidth() - foodNumber.getWidth()) / 2, 0);
		}
		
		@Override
		public void setSize(float width, float height) {
			super.setSize(width, height);
			
			//food.set
			float pointX = 0;
			for(int i = 0; i < 5; i++) {
				pointX += width / 7;
				Image im = icons[i];
				
				im.setX(pointX - im.getWidth() / 2);
				im.setY(height - im.getHeight());
				
			}
			
			UIBackground.setSize(width, height);
		}
	}
}

package com.ld30.game.View;

import com.badlogic.gdx.Gdx;
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
	
	private final Array<CityUI> cityUIs;
	private final TopUI topUI;
	
	private final float width;
	private final float height;
	
	public GameUI(final GameWorld gameWorld, final Array<City> cities, final Assets assets) {
		this.gameWorld = gameWorld;
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		stage = new Stage(new StretchViewport(width, height));
		
		cityUIs = new Array<CityUI>(cities.size);
		for(int i = 0, n = cities.size; i < n; i++) {
			City city = cities.get(i);
			CityUI group = new CityUI(city, assets);
			
			group.setSize(city.getWidth(), 50); //FIXME hardcode
			cityUIs.add(group);
			stage.addActor(group);
			group.setPosition(city.getX(), city.getY());
			
		}
		
		topUI = new TopUI(cities, assets);
		stage.addActor(topUI);
		
	}
	
	public void updateAndRender(SpriteBatch batch) {
		
		//Update
		/*for(CityUI ui : cityUIs) {
			ui.update();
			City city = ui.city;
			ui.setPosition(city.getX(), city.getY()); //TODO improve...
		}
		topUI.update();*/
		
		stage.act();
		batch.begin();
		stage.draw();
		batch.end();
	}
	
	private class TopUI extends Group {
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
		private final Label soldierNumber;
		private final Label workerNumber;
		private final Skin skin;
		
		private int foodCount;
		private int metalCount;
		private int woodCount;
		private int soldierCount;
		private int workerCount;
		
		private final Array<City> cities;
		
		public TopUI(Array<City> cities, Assets assets) {
			this.cities = cities;
			
			skin = assets.UISkin;
			
			UIBackground = new Image();
			addActor(UIBackground);
			
			food = new Image(assets.water);
			metal = new Image(assets.water);
			wood = new Image(assets.water);
			soldiers = new Image(assets.water);
			workers = new Image(assets.water);
			icons = new Image[5];
			icons[0] = food;
			icons[1] = metal;
			icons[2] = wood;
			icons[3] = soldiers;
			icons[4] = workers;
			for(int i = 0; i < 5; i++) {
				addActor(icons[i]);
			}
			
			foodNumber = new Label("", skin);
			metalNumber = new Label("", skin);
			woodNumber = new Label("", skin);
			soldierNumber = new Label("", skin);
			workerNumber = new Label("", skin);
			addActor(foodNumber);
			addActor(metalNumber);
			addActor(woodNumber);
			addActor(soldierNumber);
			addActor(workerNumber);
		}
		
		@Override
		public void act(float delta) {
			foodCount = 0;
			metalCount = 0;
			woodCount = 0;
			soldierCount = 0;
			workerCount = 0;
			
			for(City city : cities) {
				foodCount += city.getFoodCount();
				metalCount += city.getMetalCount();
				woodCount += city.getWoodCount();
				soldierCount += city.getSoldierCount();
				workerCount += city.getWorkerCount();
			}
			
			foodNumber.setText(String.valueOf(foodCount));
			metalNumber.setText(String.valueOf(metalCount));
			woodNumber.setText(String.valueOf(woodCount));
			soldierNumber.setText(String.valueOf(soldierCount));
			workerNumber.setText(String.valueOf(workerCount));
			
			foodNumber.setPosition(food.getX() + (food.getWidth() - foodNumber.getWidth()) / 2, 0);
			metalNumber.setPosition(metal.getX() + (metal.getWidth() - metalNumber.getWidth()) / 2, 0);
			woodNumber.setPosition(wood.getX() + (wood.getWidth() - woodNumber.getWidth()) / 2, 0);
			soldierNumber.setPosition(soldiers.getX() + (soldiers.getWidth() - soldierNumber.getWidth()) / 2, 0);
			workerNumber.setPosition(workers.getX() + (workers.getWidth() - workerNumber.getWidth()) / 2, 0);
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
			
			food = new Image(assets.water);
			metal = new Image(assets.water);
			wood = new Image(assets.water);
			soldiers = new Image(assets.water);
			workers = new Image(assets.water);
			icons = new Image[5];
			icons[0] = food;
			icons[1] = metal;
			icons[2] = wood;
			icons[3] = soldiers;
			icons[4] = workers;
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
		
		@Override
		public void act(float delta) {
			foodNumber.setText(String.valueOf(city.getFoodCount()));
			metalNumber.setText(String.valueOf(city.getMetalCount()));
			woodNumber.setText(String.valueOf(city.getWoodCount()));
			soldierCount.setText(String.valueOf(city.getSoldierCount()));
			workerCount.setText(String.valueOf(city.getWorkerCount()));
			
			foodNumber.setPosition(food.getX() + (food.getWidth() - foodNumber.getWidth()) / 2, 0);
			metalNumber.setPosition(metal.getX() + (metal.getWidth() - metalNumber.getWidth()) / 2, 0);
			woodNumber.setPosition(wood.getX() + (wood.getWidth() - woodNumber.getWidth()) / 2, 0);
			soldierCount.setPosition(soldiers.getX() + (soldiers.getWidth() - soldierCount.getWidth()) / 2, 0);
			workerCount.setPosition(workers.getX() + (workers.getWidth() - workerCount.getWidth()) / 2, 0);
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

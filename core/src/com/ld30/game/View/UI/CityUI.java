package com.ld30.game.View.UI;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.ld30.game.Assets;
import com.ld30.game.Model.City;
import com.ld30.game.utils.Log;

public final class CityUI extends Group {
	
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
		
		UIBackground = new Image(assets.black);
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
		super.act(delta);
		
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
		
		Log.trace(this);
		//food.set
		float pointX = 0;
		for(int i = 0; i < 5; i++) {
			pointX += width / 6;
			Image im = icons[i];
			
			im.setX(pointX - im.getWidth() / 2);
			im.setY(height - im.getHeight());
			
		}
		
		UIBackground.setSize(width, height);
	}
}
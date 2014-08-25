package com.ld30.game.View.UI;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.ld30.game.Assets;
import com.ld30.game.Model.City;

public final class TopUI extends Group {
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
		
		UIBackground = new Image(assets.black);
		UIBackground.getColor().a = 0.5f;
		addActor(UIBackground);
		
		food = new Image(assets.foodIcon);
		metal = new Image(assets.metalIcon);
		wood = new Image(assets.woodIcon);
		soldiers = new Image(assets.soldierIcon);
		workers = new Image(assets.workerIcon);
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
		super.act(delta);
		
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
			pointX += width / 6;
			Image im = icons[i];
			
			im.setX(pointX - im.getWidth() / 2);
			im.setY(height - im.getHeight() - 5);
			
		}
		
		UIBackground.setSize(width, height);
	}
}

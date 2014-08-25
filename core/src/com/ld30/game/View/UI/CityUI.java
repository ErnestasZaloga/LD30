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
	private final Label soldierNumber;
	private final Label workerNumber;
	private final Skin skin;
	
	private float widthMod = 32;
	private int decimalMod = 1;
	private int oldDecimalMod = 1;
	public boolean sizeChanged = false;
	
	public CityUI(final City city, final Assets assets) {
		this.city = city;
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
		
		int foodCount = city.getFoodCount();
		int metalCount = city.getMetalCount();
		int woodCount = city.getWoodCount();
		int soldierCount = city.getSoldierCount();
		int workerCount = city.getWorkerCount();
		
		int t = 100 * decimalMod;
		int t2 = 100 * oldDecimalMod;
		
		if(foodCount >= t || metalCount >= t || woodCount >= t || soldierCount >= t || workerCount >= t) {
			oldDecimalMod = decimalMod;
			decimalMod *= 10;
			//widthMod += 14;
			sizeChanged = true;
			setSize(getWidth() + widthMod, getHeight());
		}
		if(foodCount < t2 && metalCount < t2 && woodCount < t2 && soldierCount < t2 && workerCount < t2) {
			oldDecimalMod /= 10;
			decimalMod /= 10;
			//widthMod -= 14;
			sizeChanged = true;
			setSize(getWidth() - widthMod, getHeight());
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
		
		Log.trace(this);
		//food.set
		float pointX = width / 10;
		for(int i = 0; i < 5; i++) {
			
			Image im = icons[i];
			
			im.setX(pointX - im.getWidth() / 2);
			im.setY(height - im.getHeight() - 5);
			pointX += width / 5;
			
		}
		
		UIBackground.setSize(width, height);
	}
}
package com.ld30.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.ld30.game.Assets;
import com.ld30.game.Model.City;
import com.ld30.game.Model.GameWorld;
import com.ld30.game.utils.Log;

public class GameUI {
	private SpriteBatch batch;
	private final Stage stage;
	private final GameWorld gameWorld;
	
	private final Array<CityUI> cityUIs;
	private final TopUI topUI;
	private final Array<Actor> cityMouseOverRecievers;
	private final Array<CityButtonGroup> buttonGroups;
	
	private final float width;
	private final float height;
	
	public GameUI(final GameWorld gameWorld) {
		
		this.gameWorld = gameWorld;
		final Array<City> cities = gameWorld.getCities();
		final Assets assets = gameWorld.getAssets();
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		stage = new Stage(new StretchViewport(width, height), batch);
		Gdx.input.setInputProcessor(stage);
		
		cityUIs = new Array<CityUI>(cities.size);
		cityMouseOverRecievers = new Array<Actor>(cities.size);
		buttonGroups = new Array<CityButtonGroup>(cities.size);
		
		//Set up city uis.
		for(int i = 0, n = cities.size; i < n; i++) {
			City city = cities.get(i);
			CityUI group = new CityUI(city, assets);
			group.setTransform(false);
			group.setSize(100, 20);//FIXME hardcode
			cityUIs.add(group);
			stage.addActor(group);
			group.setPosition(city.getX() - 10, city.getY() - 10);
			
			final CityButtonGroup bg = new CityButtonGroup(city, assets);
			buttonGroups.add(bg);
			bg.setPosition(city.getX(), city.getY());
			bg.setSize(100, 150);//FIXME hardcode again
			bg.setTransform(false);
			
			
			final Actor actor = new Actor() {
				@Override
				public void act(float delta) {
					float x = Gdx.input.getX();
					float y = Gdx.graphics.getHeight() - Gdx.input.getY();
					if(x >= getX() && x <= getRight() && y >=getY() && y <= getTop()) {
						if(!bg.hasParent())
						stage.addActor(bg);
					} else if(!(x >= bg.getX() && x <= bg.getRight() && y >= bg.getY() && y <= bg.getTop())) {
						bg.remove();
					}
				}
			};
			actor.setBounds(city.getX(), city.getY(), city.getWidth(), city.getHeight());
			/*actor.addListener(new InputListener() {
				@Override
				public boolean mouseMoved(InputEvent event, float x, float y) {
					if(x >= actor.getX() && x <= actor.getRight()) {
						if(y >= actor.getY() && y <= actor.getTop()) {
							if(bg.hasParent())
							stage.addActor(bg);
						}
					} else {
						bg.remove();
					}
					
					return false;
				}
				
				@Override
				public void touchDragged (InputEvent event, float x, float y, int pointer) {
					mouseMoved(event, x, y);
				}
			});*/
			cityMouseOverRecievers.add(actor);
			stage.addActor(actor);
		}
		
		topUI = new TopUI(cities, assets);
		topUI.setTransform(false);
		stage.addActor(topUI);
		topUI.setSize(500, 60);
		topUI.setPosition(0, stage.getHeight() - topUI.getHeight());
		
	}
	
	public void updateAndRender(SpriteBatch batch) {
		
		//Update
		/*for(CityUI ui : cityUIs) {
			ui.update();
			City city = ui.city;
			ui.setPosition(city.getX(), city.getY()); //TODO improve...
		}
		topUI.update();*/
		//Gdx.gl.glClearColor(1, 1, 1, 1); //FIXME REMOVE!!!!!!!!!!!!!!!!!!!
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		batch.begin();
		stage.draw();
		batch.end();
	}
	
	private class CityButtonGroup extends Group {
		private final Image UIBackground;
		private final Skin skin;
		
		private final Label trainSoldier;
		private final Label trainWorker;
		
		public CityButtonGroup(final City city, final Assets assets) {
			skin = assets.UISkin;
			
			UIBackground = new Image(assets.road);
			addActor(UIBackground);
			
			trainSoldier = new Label("   TRAIN\n SOLDIER", skin);
			trainSoldier.addListener(new InputListener() {
				@Override
				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					if(x >= trainSoldier.getX() && x <= trainSoldier.getRight()) {
						if(y >= trainSoldier.getY() && y <= trainSoldier.getTop()) {
							city.makeSoldier();
						}
					}
				}
			});
			addActor(trainSoldier);
			trainWorker = new Label("   TRAIN\n WORKER", skin);
			trainWorker.addListener(new InputListener() {
				@Override
				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					if(x >= trainWorker.getX() && x <= trainWorker.getRight()) {
						if(y >= trainWorker.getY() && y <= trainWorker.getTop()) {
							city.makeWorker();
						}
					}
				}
			});
			addActor(trainWorker);
		}
		
		@Override
		public void setSize(float width, float height) {
			super.setSize(width, height);
			
			UIBackground.setSize(width, height);
			
			trainSoldier.setPosition(0, 0);//FIXME quick align
			trainWorker.setPosition(0, height - trainWorker.getHeight());
		}
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
			
			UIBackground = new Image(assets.road);
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
				pointX += width / 7;
				Image im = icons[i];
				
				im.setX(pointX - im.getWidth() / 2);
				im.setY(height - im.getHeight());
				
			}
			
			UIBackground.setSize(width, height);
		}
	}
	
	public class Label extends com.badlogic.gdx.scenes.scene2d.ui.Label {

		public Label(CharSequence text, Skin skin) {
			super(text, skin);
		}
		
		public Label(CharSequence text, Skin skin, Color color) {
			super(text, skin);
		}
		
		@Override
		public void setText(CharSequence text) {
			super.setText(text);
			pack();
		}
		
	}
}

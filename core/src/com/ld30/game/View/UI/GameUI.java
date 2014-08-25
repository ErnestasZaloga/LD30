package com.ld30.game.View.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.ld30.game.Assets;
import com.ld30.game.Model.City;
import com.ld30.game.Model.GameWorld;
import com.ld30.game.utils.Log;

public class GameUI {
	private enum State {
		SENDING_WORKERS, SENDING_SOLDIERS, NORMAL, WAITING_RE_MOUSE_OVER, SENDING_RESOURCES,
		GAME_OVER;
	}
	private State state;
	private GameOverUI gameOverUI;
	
	private SpriteBatch batch;
	private final Stage stage;
	//private final GameWorld gameWorld;
	
	private final Array<CityUI> cityUIs;
	private final TopUI topUI;
	private final Array<Actor> cityMouseOverRecievers;
	private final Array<CityButtonGroup> buttonGroups;
	
	private final float screenW;
	private final float screenH;
	
	private int unitCount = 0;
	private final TextButton countChanger; 
	
	private City unitSenderCity;
	private City recieverCity;
	
	final Array<City> cities;
	
	public GameUI(final GameWorld gameWorld) {
		state = State.NORMAL;
		
		//this.gameWorld = gameWorld;
		cities = gameWorld.getCities();
		final Assets assets = gameWorld.getAssets();
		
		screenW = Gdx.graphics.getWidth();
		screenH = Gdx.graphics.getHeight();
		
		stage = new Stage(new StretchViewport(screenW, screenH), batch) {
			@Override
			public void act(float delta) {
				super.act(delta);
				
				for(CityUI c : cityUIs) {
					if(c.sizeChanged) {
						positionCities();
					}
				}
				
				if(state == State.GAME_OVER && !gameOverUI.hasParent()) {
					gameOverUI.update();
					stage.addActor(gameOverUI);
				} else if(gameOverUI.hasParent() && state != State.GAME_OVER) {
					gameOverUI.remove();
				}
			}
		};
		Gdx.input.setInputProcessor(stage);
		Actor r = new Actor();
		r.setSize(screenW, screenH);
		r.addListener(new InputListener() {
			
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				recieverCity = null;
				return clearSendState();
			}
		});
		stage.addActor(r);
		
		countChanger = new TextButton("Count: " + unitCount, assets.UISkin) {
			@Override
			public void act(float delta) {
				super.act(delta);
				
				float x = Gdx.input.getX();
				float y = Gdx.graphics.getHeight() - Gdx.input.getY();
				
				countChanger.setPosition(x + 10, y - countChanger.getHeight() - 20); //FIXME dirty
				
				if(state == State.SENDING_WORKERS) {
					if(unitCount > unitSenderCity.getWorkerCount()) {
						unitCount = unitSenderCity.getWorkerCount();
					}
				} else if(state == State.SENDING_SOLDIERS) {
					if(unitCount > unitSenderCity.getSoldierCount()) {
						unitCount = unitSenderCity.getSoldierCount();
					}
				} else if(state == State.SENDING_RESOURCES) {
					GameWorld.ResourceType type = unitSenderCity.getType();
					int resource;
					if(type == GameWorld.ResourceType.FOOD) {
						resource = unitSenderCity.getFoodCount();
					} else if(type == GameWorld.ResourceType.IRON) {
						resource = unitSenderCity.getMetalCount();
					} else if(type == GameWorld.ResourceType.WOOD) {
						resource = unitSenderCity.getWoodCount();
					} else {
						throw new IllegalArgumentException("Sending resource from NONE type city");
					}
					int wc = unitSenderCity.getWorkerCount();
					int rwc = resource /  City.RESOURCE_PER_WORKER;
					
					if(unitCount > wc || unitCount > rwc) {
						unitCount = wc < rwc ? wc : rwc;
					}
				}
				
				if(unitCount < 0) {
					unitCount = 0;
				}
				countChanger.setText("Count: " + unitCount);
				countChanger.pack();
			}
		};
		stage.addListener(new InputListener() {
			@Override
			public boolean scrolled (InputEvent event, float x, float y, int amount) {
				if(countChanger.hasParent())
				unitCount -= amount;
				state = State.GAME_OVER;//FIXME  debug
				
				return true;
			}
		});
		countChanger.pack();
		
		cityUIs = new Array<CityUI>(cities.size);
		cityMouseOverRecievers = new Array<Actor>(cities.size);
		buttonGroups = new Array<CityButtonGroup>(cities.size);
		
		//Set up city uis.
		for(int i = 0, n = cities.size; i < n; i++) {
			//final int ii = i;
			
			final City city = cities.get(i);
			CityUI cityUI = new CityUI(city, assets);
			cityUI.setTransform(false);
			cityUI.setSize(180, 40);//FIXME hardcode
			cityUIs.add(cityUI);
			stage.addActor(cityUI);
			
			final CityButtonGroup bg = new CityButtonGroup(city, assets);
			buttonGroups.add(bg);
			
			bg.setSize(100, 278);//FIXME hardcode again
			bg.setPosition((city.getWidth() - bg.getWidth()) / 2 + city.getX(), 
							(city.getHeight() - bg.getHeight()) / 2 + city.getY());
			
			if(bg.getX() < 0) 			
				bg.setX(0);
			
			if(bg.getRight() > screenW) 	
				bg.setX(screenW - bg.getWidth());
			
			if(bg.getY() < 0) 			
				bg.setY(0);
			
			if(bg.getTop() > screenH) 	
				bg.setY(screenH - bg.getHeight());
			
			bg.setTransform(false);
			
			
			final Actor cityMouseOverReciever = new Actor() {
				@Override
				public void act(float delta) {
					/*if() {
						return;
					}*/
					float x = Gdx.input.getX();
					float y = Gdx.graphics.getHeight() - Gdx.input.getY();
					if(x >= getX() && x <= getRight() && y >=getY() && y <= getTop() && state == State.NORMAL) {
						if(!bg.hasParent())
						stage.addActor(bg);
					} else if(!(x >= bg.getX() && x <= bg.getRight() && y >= bg.getY() && y <= bg.getTop())) {
						bg.remove();
						if(state == State.WAITING_RE_MOUSE_OVER && (bg.city == recieverCity || recieverCity == null)) {
							state = State.NORMAL;
						}
					}
				}
			};
			cityMouseOverReciever.addListener(new InputListener() {
				@Override
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					if(state == State.NORMAL) {
						return true;
					} else if (state == State.SENDING_SOLDIERS){
						unitSenderCity.sendSoldiersTo(gameWorld, city, unitCount);
						recieverCity = city;
						return clearSendState();
					} else if(state == State.SENDING_WORKERS) {
						unitSenderCity.sendWorkersTo(gameWorld, city, unitCount, false);
						recieverCity = city;
						return clearSendState();
					} else if(state == State.SENDING_RESOURCES) {
						unitSenderCity.sendWorkersTo(gameWorld, city, unitCount, true);
						recieverCity = city;
						return clearSendState();
					}
					
					return true;
				}
			});
			cityMouseOverReciever.setBounds(city.getX(), city.getY(), city.getWidth(), city.getHeight());
			cityMouseOverRecievers.add(cityMouseOverReciever);
			stage.addActor(cityMouseOverReciever);
		}
		
		topUI = new TopUI(cities, assets);
		topUI.setTransform(false);
		stage.addActor(topUI);
		topUI.setSize(500, 43);
		topUI.setPosition((screenW - topUI.getWidth()) / 2, screenH - topUI.getHeight());
		
		positionCities();
		
		gameOverUI = new GameOverUI(assets, cities);
		gameOverUI.setSize(screenW, screenH);
	}
	
	public void positionCities() {
		City topCity = null;
		float top = 0;
		int ii = 0;
		int a = 0;
		for(City city : cities) {
			if(city.getY() > top) {
				topCity = city;
				top = city.getY();
				ii = a;
			}
			a++;
		}
		
		cityUIs.get(ii).setY(topCity.getY() + topCity.getHeight());
		for(int i = 0, n = cities.size; i < n; i++) {
			final CityUI ui = cityUIs.get(i);
			final City city = cities.get(i);
			
			ui.setX((city.getWidth() - ui.getWidth()) / 2 + city.getX());
			
			ui.setY(city.getY() <= (screenH - city.getHeight()) / 2 ? 
							   city.getY() - ui.getHeight() : city.getY() + city.getHeight());
			
			if(city == topCity) {
				ui.setY(city.getY() + city.getHeight());
			}
			
			if(ui.getX() < 0) 			
				ui.setX(0);
			
			if(ui.getRight() > screenW) 	
				ui.setX(screenW - ui.getWidth());
			
			if(ui.getY() < 0) 			
				ui.setY(city.getY() + city.getHeight());
			
			if(ui.getTop() > topUI.getY()) 	
				ui.setY(city.getY() - ui.getHeight());
		}
	}
	
	public boolean clearSendState() {
		Log.trace(this);
		unitCount = 0;
		countChanger.setText("Count: " + unitCount);
		countChanger.pack();
		countChanger.remove();
		unitSenderCity = null;
		state = State.WAITING_RE_MOUSE_OVER;
		return true;
	}
	
	public void updateAndRender(SpriteBatch batch) {
		stage.act(Gdx.graphics.getDeltaTime());
		batch.begin();
		stage.draw();
		batch.end();
	}
	
	private class CityButtonGroup extends Group {
		private final Image UIBackground;
		private final Skin skin;
		
		private final TextButton trainSoldier;
		private final TextButton trainWorker;
		private final TextButton sendSoldier;
		private final TextButton sendWorker;
		private final TextButton transportResources;
		private final City city;
		
		
		public CityButtonGroup(final City city, final Assets assets) {
			skin = assets.UISkin;
			this.city = city;
			
			
			UIBackground = new Image(assets.black);
			//addActor(UIBackground);
			
			transportResources = new TextButton("TRANSPORT\n RESOURCES", skin);
			transportResources.addListener(new InputListener() {
				@Override
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					state = State.SENDING_RESOURCES;
					unitSenderCity = city;
					stage.addActor(countChanger);
					
					return true;
				}
			});
			addActor(transportResources);
			
			trainSoldier = new TextButton("TRAIN\n SOLDIER", skin);
			trainSoldier.addListener(new InputListener() {
				@Override
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					city.makeSoldier();
					
					return true;
				}
			});
			addActor(trainSoldier);
			trainWorker = new TextButton("TRAIN\n WORKER", skin);
			trainWorker.addListener(new InputListener() {
				@Override
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					city.makeWorker();
						
					return true;
				}
			});
			addActor(trainWorker);
			sendSoldier = new TextButton("SEND\n SOLDIERS", skin);
			sendSoldier.addListener(new InputListener() {
				@Override
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					state = State.SENDING_SOLDIERS;
					unitSenderCity = city;
					stage.addActor(countChanger);
					
					return true;
				}
			});
			addActor(sendSoldier);
			sendWorker = new TextButton("SEND\n WORKERS", skin);
			sendWorker.addListener(new InputListener() {
				@Override
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					state = State.SENDING_WORKERS;
					unitSenderCity = city;
					stage.addActor(countChanger);
					
					return true;
				}
			});
			addActor(sendWorker);
			
		}
		
		@Override
		public void setSize(float width, float height) {
			super.setSize(width, height);
			
			UIBackground.setSize(width, height);
			
			transportResources.setPosition(0, 0);
			
			trainSoldier.setPosition(0, height - trainSoldier.getHeight());
			trainWorker.setPosition(0, trainSoldier.getY() - trainWorker.getHeight());
			
			float p = (transportResources.getTop() + trainWorker.getY()) / 2;
			
			sendWorker.setPosition(0, p - sendWorker.getHeight());
			sendSoldier.setPosition(0, p);
			
			
			sendSoldier.setWidth(transportResources.getWidth());
			trainSoldier.setWidth(transportResources.getWidth());
			trainWorker.setWidth(transportResources.getWidth());
			sendWorker.setWidth(transportResources.getWidth());
			//trainWorker.setWidth(transportResources.getWidth());
			/*trainSoldier.setPosition(0, 0);//FIXME quick align
			trainWorker.setPosition(0, height - trainWorker.getHeight());*/
		}
	}
}

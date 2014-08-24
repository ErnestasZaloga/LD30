package com.ld30.game.View.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.ld30.game.Assets;
import com.ld30.game.Model.City;
import com.ld30.game.Model.GameWorld;
import com.ld30.game.utils.Log;

public class GameUI {
	private enum State {
		SENDING_WORKERS, SENDING_SOLDIERS, NORMAL, WAITING_RE_MOUSE_OVER;
	}
	private State state;
	
	private SpriteBatch batch;
	private final Stage stage;
	//private final GameWorld gameWorld;
	
	private final Array<CityUI> cityUIs;
	private final TopUI topUI;
	private final Array<Actor> cityMouseOverRecievers;
	private final Array<CityButtonGroup> buttonGroups;
	
	private final float width;
	private final float height;
	
	private int unitCount = 0;
	private final TextButton countChanger; 
	
	private City unitSenderCity;
	private City recieverCity;
	
	public GameUI(final GameWorld gameWorld) {
		state = State.NORMAL;
		
		//this.gameWorld = gameWorld;
		final Array<City> cities = gameWorld.getCities();
		final Assets assets = gameWorld.getAssets();
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		stage = new Stage(new StretchViewport(width, height), batch);
		Gdx.input.setInputProcessor(stage);
		Actor r = new Actor();
		r.setSize(width, height);
		r.addListener(new InputListener() {
			
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
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
			}
		};
		stage.addListener(new InputListener() {
			@Override
			public boolean scrolled (InputEvent event, float x, float y, int amount) {
				unitCount -= amount;
				countChanger.setText("Count: " + unitCount);
				countChanger.pack();
				
				return true;
			}
		});
		countChanger.pack();
		
		cityUIs = new Array<CityUI>(cities.size);
		cityMouseOverRecievers = new Array<Actor>(cities.size);
		buttonGroups = new Array<CityButtonGroup>(cities.size);
		
		//Set up city uis.
		for(int i = 0, n = cities.size; i < n; i++) {
			final int ii = i;
			
			final City city = cities.get(i);
			CityUI group = new CityUI(city, assets);
			group.setTransform(false);
			group.setSize(100, 20);//FIXME hardcode
			cityUIs.add(group);
			stage.addActor(group);
			group.setPosition(city.getX() - 10, city.getY() - 10);
			
			final CityButtonGroup bg = new CityButtonGroup(city, assets);
			buttonGroups.add(bg);
			bg.setPosition(city.getX(), city.getY());
			bg.setSize(100, 250);//FIXME hardcode again
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
						if(state == State.WAITING_RE_MOUSE_OVER && bg.city == recieverCity) {
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
						unitSenderCity.sendSoldiersTo(city, unitCount); //TODO count
						recieverCity = city;
						return clearSendState();
					} else if(state == State.SENDING_WORKERS) {
						unitSenderCity.sendWorkersTo(city, unitCount);//TODO count
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
		topUI.setSize(500, 60);
		topUI.setPosition(0, stage.getHeight() - topUI.getHeight());
		
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
		//private final TextButton transportResources;
		private final City city;
		
		
		public CityButtonGroup(final City city, final Assets assets) {
			skin = assets.UISkin;
			this.city = city;
			
			
			UIBackground = new Image(assets.black);
			addActor(UIBackground);
			
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
			sendSoldier = new TextButton("SEND\n SOLDIER", skin);
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
			sendWorker = new TextButton("SEND\n WORKER", skin);
			sendWorker.addListener(new InputListener() {
				@Override
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					state = State.SENDING_WORKERS;
					unitSenderCity = city;
					
					return true;
				}
			});
			addActor(sendWorker);
			
		}
		
		@Override
		public void setSize(float width, float height) {
			super.setSize(width, height);
			
			UIBackground.setSize(width, height);
			
			trainSoldier.setPosition(0, 0);
			sendSoldier.setPosition(0, height / 4f);
			sendWorker.setPosition(0, height / 2f);
			trainWorker.setPosition(0, height * 3f / 4f);
			/*trainSoldier.setPosition(0, 0);//FIXME quick align
			trainWorker.setPosition(0, height - trainWorker.getHeight());*/
		}
	}
}

package com.ld30.game.Model;

import com.badlogic.gdx.utils.Array;
import com.ld30.game.Assets;
import com.ld30.game.Model.WorldGenerator.GeneratedWorld;
import com.ld30.game.Model.Tiles.Road;
import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.Model.moveable.MovableManager;
import com.ld30.game.Model.moveable.Worker;
import com.ld30.game.View.GameUI;
import com.ld30.game.utils.AStar;
import com.ld30.game.utils.Log;

public class GameWorld {

	private Assets assets;
	private Map map = new Map();
	private Array<MoveableEntity> entities = new Array<MoveableEntity>();
	private Array<City> cities = new Array<City>();
	private AStar astar = new AStar();
	private MovableManager movableManager;
	private Tile[] cityCenters;
	
	private GameUI gameUI;
	
	public enum Center {
		WOOD, FOOD, IRON, NONE
	}
	
	public GameWorld(Assets assets) {
		this.assets = assets;
		map.setTileWidth(assets.grass.getRegionWidth());
		map.setTileHeight(assets.grass.getRegionHeight());
		
		movableManager = new MovableManager(this);
	}
	
	public void begin() {
		GeneratedWorld generatedWorld = WorldGenerator.generateMap(assets, map.getTileWidth(), astar);
		map.setTiles(generatedWorld.tiles);

		cityCenters = generatedWorld.centers;
		
		astar.setSize(map.getWidth(), map.getHeight());
		
		Worker worker = new Worker ();
		worker.setTexture(assets.moveable);
		worker.setPixelsPerSecond(256);
		
		entities.add(worker);
		
		for (int i = 0; i < cityCenters.length; i += 1) {
			final Road road = (Road) cityCenters[i];
			
			Log.trace(this, road.getWidth(), assets.city.getRegionWidth());
			City city = new City (
					assets.city, 
					road.getX() + road.getWidth() / 2f - assets.city.getRegionWidth() / 2f, 
					road.getY() + road.getHeight() / 2f - assets.city.getRegionHeight() / 2f, 
					road.getCenter());
			cities.add(city);
		}
		gameUI = new GameUI(this);
	}
	
	public void update(float delta) {
		movableManager.update(delta);
		
		for (int i = 0; i < cities.size; i += 1) {
			cities.get(i).update(delta);
		}
	}

	public Array<MoveableEntity> getEntities() {
		return entities;
	}

	public void setEntities(Array<MoveableEntity> entities) {
		this.entities = entities;
	}

	public Array<City> getCities() {
		return cities;
	}

	public void setCities(Array<City> cities) {
		this.cities = cities;
	}

	public Map getMap () {
		return map;
	}
	
	public AStar getAStar () {
		return astar;
	}
	
	public Tile[] getCityCenters () {
		return cityCenters;
	}
	
	public Assets getAssets() {
		return assets;
	}
	
	public GameUI getUI() {
		return gameUI;
	}
	
}

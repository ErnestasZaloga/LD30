package com.ld30.game.Model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.ld30.game.Assets;
import com.ld30.game.Model.WorldGenerator.GeneratedWorld;
import com.ld30.game.Model.moveable.MovableManager;
import com.ld30.game.Model.moveable.Worker;
import com.ld30.game.utils.AStar;

public class GameWorld {

	private Assets assets;
	private Map map = new Map();
	private Array<MoveableEntity> entities = new Array<MoveableEntity>();
	private Array<City> cities = new Array<City>();
	private AStar astar = new AStar();
	private MovableManager movableManager;
	
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
		GeneratedWorld generatedWorld = WorldGenerator.generateMap(assets, map.getTileWidth());
		map.setTiles(generatedWorld.tiles);
		astar.setSize(map.getWidth(), map.getHeight());
		
		Worker worker = new Worker ();
		worker.setTexture(assets.moveable);
		worker.setLastPosition(0, 0);
		worker.setDestination(MathUtils.random(0, map.getWidth() - 1), MathUtils.random(0, map.getHeight() - 1));
		worker.setPixelsPerSecond(64);
		movableManager.move(worker);
		
		entities.add(worker);
	}
	
	public void update(float delta) {
		movableManager.update(delta);
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
	
}

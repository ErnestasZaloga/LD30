package com.ld30.game.Model;

import com.badlogic.gdx.utils.Array;
import com.ld30.game.Assets;

public class GameWorld {

	private Assets assets;
	private Map map = new Map();
	private Array<MoveableEntity> entities = new Array<MoveableEntity>();
	private Array<City> cities = new Array<City>();
	
	public GameWorld(Assets assets) {
		this.assets = assets;
		map.setTileWidth(assets.tile.getRegionWidth());
		map.setTileHeight(assets.tile.getRegionHeight());
	}
	
	public void begin() {
		map.setTiles(WorldGenerator.generateMap(assets));
	}
	
	public void update(float delta) {
		
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

	public Map getMap() {
		return map;
	}
	
}

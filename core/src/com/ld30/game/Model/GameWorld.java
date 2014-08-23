package com.ld30.game.Model;

import com.badlogic.gdx.utils.Array;
import com.ld30.game.Assets;

public class GameWorld {

	private Assets assets;
	private Array<Tile> tiles = new Array<Tile>();
	private Array<MoveableEntity> entities = new Array<MoveableEntity>();
	private Array<City> cities = new Array<City>();
	
	public GameWorld(Assets assets) {
		this.assets = assets;
	}
	
	public void begin() {
		tiles = WorldGenerator.generateMap(assets);
	}
	
	public void update(float delta) {
		
	}
	public Array<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(Array<Tile> tiles) {
		this.tiles = tiles;
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
}

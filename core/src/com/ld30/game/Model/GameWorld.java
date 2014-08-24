package com.ld30.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.ld30.game.Assets;
import com.ld30.game.Model.WorldGenerator.GeneratedWorld;
import com.ld30.game.Model.Tiles.Road;
import com.ld30.game.Model.moveable.MovableManager;
import com.ld30.game.Model.moveable.OrcManager;
import com.ld30.game.View.UI.GameUI;
import com.ld30.game.utils.AStar;
import com.ld30.game.utils.Log;



public class GameWorld {

	private final Blockade blockadeFromFoodToIron = new Blockade();
	private final Blockade blockadeFromIronToWood = new Blockade();
	private final Blockade blockadeFromWoodToFood = new Blockade();
	private final Blockade[] blockades = new Blockade[] {
			blockadeFromFoodToIron,
			blockadeFromIronToWood,
			blockadeFromIronToWood
	};
	
	private Assets assets;
	private Map map = new Map();
	private Array<MoveableEntity> entities = new Array<MoveableEntity>();
	private Array<City> cities = new Array<City>();
	private AStar astar = new AStar();
	private MovableManager movableManager;
	private OrcManager orcManager;
	
	private GameUI gameUI;
	
	public static enum ResourceType {
		WOOD, FOOD, IRON, NONE
	}
	
	public GameWorld(Assets assets) {
		this.assets = assets;
		map.setTileWidth(assets.grass.getRegionWidth());
		map.setTileHeight(assets.grass.getRegionHeight());
		
		movableManager = new MovableManager(this);
		orcManager = new OrcManager(this);
	}
	
	public void begin() {
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		
		int numberOfTilesVertically = 64;
		int numberOfTilesHorizontally;
		
		float tileSize = screenHeight/numberOfTilesVertically;
		final float tileWH = 16;
		float tileScale = tileSize/tileWH;
		map.setTileScale(tileScale);
		map.setTileHeight(tileSize);
		map.setTileWidth(tileSize);
		numberOfTilesHorizontally = (int) Math.ceil(screenWidth/tileSize);
		
		GeneratedWorld generatedWorld = WorldGenerator.generateMap(assets, map.getTileWidth(), astar, numberOfTilesHorizontally, numberOfTilesVertically);
		map.setTiles(generatedWorld.tiles);
		
		astar.setSize(map.getWidth(), map.getHeight());
		
		for (int i = 0; i < generatedWorld.centers.length; i += 1) {
			final Road road = (Road) generatedWorld.centers[i];
			
			Log.trace(this, road.getWidth(), assets.city.getRegionWidth());
			City city = new City (
					this,
					assets.city, 
					road.getX() + road.getWidth() / 2f - assets.city.getRegionWidth() / 2f, 
					road.getY() + road.getHeight() / 2f - assets.city.getRegionHeight() / 2f, 
					road.getCenter(),
					generatedWorld.centers[i]);
			
			cities.add(city);
		}
		
		generatedWorld.getRoadFromFoodToIron();
		generatedWorld.getRoadFromIronToWood();
		generatedWorld.getRoadWoodToFood();

		blockadeFromFoodToIron.setTile(generatedWorld.getRoadFromFoodToIron().get(generatedWorld.getRoadFromFoodToIron().size / 2));
		blockadeFromIronToWood.setTile(generatedWorld.getRoadFromIronToWood().get(generatedWorld.getRoadFromIronToWood().size / 2));
		blockadeFromWoodToFood.setTile(generatedWorld.getRoadWoodToFood().get(generatedWorld.getRoadWoodToFood().size / 2));
		
		gameUI = new GameUI(this);
	}
	
	public void update(float delta) {
		orcManager.update(delta);
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
	
	public Assets getAssets() {
		return assets;
	}
	
	public GameUI getUI() {
		return gameUI;
	}
	
	public Blockade[] getBlockades () {
		return blockades;
	}

	public Blockade getBlockadeFromFoodToIron() {
		return blockadeFromFoodToIron;
	}

	public Blockade getBlockadeFromIronToWood() {
		return blockadeFromIronToWood;
	}

	public Blockade getBlockadeFromWoodToFood() {
		return blockadeFromWoodToFood;
	}
	
	public MovableManager getMovableManager () {
		return movableManager;
	}

	public OrcManager getOrcManager() {
		return orcManager;
	}

}

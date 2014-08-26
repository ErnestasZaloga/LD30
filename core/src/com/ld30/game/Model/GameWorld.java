package com.ld30.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.ld30.game.Assets;
import com.ld30.game.Model.WorldGenerator.GeneratedWorld;
import com.ld30.game.Model.Tiles.Road;
import com.ld30.game.Model.Tiles.Rock;
import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.Model.Tiles.Tree;
import com.ld30.game.Model.Tiles.Water;
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
			blockadeFromWoodToFood
	};
	
	private Assets assets;
	private Map map = new Map();
	private Array<MoveableEntity> entities = new Array<MoveableEntity>();
	private Array<City> cities = new Array<City>();
	private Array<Decal> groundDecals = new Array<Decal>();
	private Array<Decal> decals = new Array<Decal>();
	private AStar astar = new AStar();
	private MovableManager movableManager;
	private OrcManager orcManager;
	private GeneratedWorld generatedWorld;
	
	private boolean gameEnded = false;
	
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
		
		int numberOfTilesVertically = 40;
		int numberOfTilesHorizontally;
		
		float tileSize = screenHeight/numberOfTilesVertically;
		final float tileWH = 16;
		float tileScale = tileWH / tileSize;
		map.setTileScale(tileScale);
		map.setTileHeight(tileSize);
		map.setTileWidth(tileSize);
		numberOfTilesHorizontally = (int) Math.ceil(screenWidth/tileSize);
		
		generatedWorld = WorldGenerator.generateMap(assets, map.getTileWidth(), astar, numberOfTilesHorizontally, numberOfTilesVertically);
		map.setTiles(generatedWorld.tiles);
		map.setPixelsPerSecond(tileSize * 3);
		
		astar.setSize(map.getWidth(), map.getHeight());
		
		for (int i = 0; i < generatedWorld.centers.length; i += 1) {
			final Road road = (Road) generatedWorld.centers[i];
			
			TextureRegion region = null;
			switch (road.getType()) {
				case WOOD:
					region = assets.woodCity;
					break;
				case IRON:
					region = assets.ironCity;
					break;
				case FOOD:
					region = assets.foodCity;
					break;
				default:
					break;
			}
			
			final float cityWidth = region.getRegionWidth() * map.getTileScale();
			final float cityHeight = region.getRegionHeight() * map.getTileScale();
			
			City city = new City (
					this,
					region, 
					road.getX() + road.getWidth() / 2f - cityWidth / 2f, 
					road.getY() + road.getHeight() / 2f - cityHeight / 2f, 
					road.getType(),
					generatedWorld.centers[i]);
			
			cities.add(city);
			city.scale(map.getTileScale());
			
			final Tile center = generatedWorld.centers[i];
			final int centerX = (int)(map.getWidth() * (center.getX() / (map.getWidth() * map.getTileWidth())));
			final int centerY = (int)(map.getHeight() * (center.getY() / (map.getHeight() * map.getTileHeight())));
			
			final float cityX = city.getX();
			final float cityY = city.getY();
			final float cityTop = cityY + city.getHeight();
			final float cityRight = cityX + city.getWidth();
			
			final int mapWidth = map.getWidth();
			final int mapHeight = map.getHeight();
			
			// Find top tile
			int iter = centerY + 1;
			int topTileY = iter;
			for (; iter < mapHeight; iter += 1) {
				final Tile tile = map.getTile(centerX, iter);
				if (tile.getY() > cityTop) {
					topTileY = iter;
					break;
				}
			}			
			
			// Find bottom tile
			iter = centerY - 1;
			int bottomTileY = iter;
			for (; iter >= 0; iter -= 1) {
				final Tile tile = map.getTile(centerX, iter);
				if (tile.getY() < cityY) {
					bottomTileY = iter;
					break;
				}
			}	
			
			// Find right tile
			iter = centerX + 1;
			int rightTileX = iter;
			for (; iter < mapWidth; iter += 1) {
				final Tile tile = map.getTile(iter, centerY);
				if (tile.getX() > cityRight) {
					rightTileX = iter;
					break;
				}
			}
			
			// Find left tile
			iter = centerX - 1;
			int leftTileX = iter;
			for (; iter >= 0; iter -= 1) {
				final Tile tile = map.getTile(iter, centerY);
				if (tile.getX() < cityX) {
					leftTileX = iter;
					break;
				}
			}
			
			final Array<Tile> citySurroundingTiles = new Array<Tile>();
			for (int ii = leftTileX; ii <= rightTileX; ii += 1) {
				Tile tile = map.getTile(ii, bottomTileY);
				if (!(tile instanceof Water) && !(tile instanceof Rock) && !(tile instanceof Tree)) {
					citySurroundingTiles.add(tile);
				}
				tile = map.getTile(ii, topTileY);
				if (!(tile instanceof Water) && !(tile instanceof Rock) && !(tile instanceof Tree)) {
					citySurroundingTiles.add(tile);
				}
			}
			for (int ii = bottomTileY + 1, nn = topTileY - 1; ii <= nn; ii += 1) {
				Tile tile = map.getTile(leftTileX, ii);
				if (!(tile instanceof Water) && !(tile instanceof Rock) && !(tile instanceof Tree)) {
					citySurroundingTiles.add(tile);
				}
				tile = map.getTile(rightTileX, ii);
				if (!(tile instanceof Water) && !(tile instanceof Rock) && !(tile instanceof Tree)) {
					citySurroundingTiles.add(tile);
				}
			}
			
			if (citySurroundingTiles.size == 0) {
				throw new RuntimeException("No tiles found");
			}
			
			city.setCitySurroundingTiles(citySurroundingTiles);
		}
		
		Log.trace(this, "Ended generation");
		
		generatedWorld.getRoadFromFoodToIron();
		generatedWorld.getRoadFromIronToWood();
		generatedWorld.getRoadWoodToFood();

		blockadeFromFoodToIron.setTile(generatedWorld.getRoadFromFoodToIron().get(generatedWorld.getRoadFromFoodToIron().size / 2));
		blockadeFromIronToWood.setTile(generatedWorld.getRoadFromIronToWood().get(generatedWorld.getRoadFromIronToWood().size / 2));
		blockadeFromWoodToFood.setTile(generatedWorld.getRoadWoodToFood().get(generatedWorld.getRoadWoodToFood().size / 2));
		
		gameUI = new GameUI(this);
	}
	
	public void update(float delta) {
		final Tile[][] tiles = map.getTiles();
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				tiles[x][y].update(delta);
			}
		}
		
		for (int i = 0; i < groundDecals.size; i += 1) {
			if (groundDecals.get(i).update(delta)) {
				groundDecals.removeIndex(i);
				--i;
			}
		}
		
		for (int i = 0; i < decals.size; i += 1) {
			if (decals.get(i).update(delta)) {
				decals.removeIndex(i);
				--i;
			}
		}

		if (gameEnded) {
			return;
		}
		
		orcManager.update(delta);
		movableManager.update(delta);
		
		int totalWorkers = 0;
		int canBuildWorkersCount = 0;
		
		for (int i = 0; i < cities.size; i += 1) {
			final City city = cities.get(i);
			city.update(delta);
			
			totalWorkers += city.getWorkerCount() + city.getPendingWorkers() + city.getPendingSpawns();
			canBuildWorkersCount += (city.getWoodCount() >= City.WORKER_WOOD_COST && 
									 city.getMetalCount() >= City.WORKER_METAL_COST &&
									 city.getFoodCount() >= City.WORKER_FOOD_COST) ? 1 : 0;
		}
		
		if (totalWorkers == 0 && canBuildWorkersCount == 0) {
			gameEnded = true;
			City.PENDING_WORKERS = 0;
			gameUI.setToGameOver();
			gameUI.getTopUI().getTimer().stop();
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

	public Array<Decal> getDecals () {
		return decals;
	}
	public Array<Decal> getGroundDecals() {
		return groundDecals;
	}

	public GeneratedWorld getGeneratedWorld() {
		return generatedWorld;
	}

}

package com.ld30.game.Model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.Model.moveable.BruteOrc;
import com.ld30.game.Model.moveable.Humanoid;
import com.ld30.game.Model.moveable.PlayerHumanoid;
import com.ld30.game.Model.moveable.Troop;
import com.ld30.game.Model.moveable.Worker;
import com.ld30.game.utils.Log;

public class City extends Entity {
	public static final int RESOURCE_PER_WORKER = 2;
	
	private static final float PEOPLE_DIE_TIME = 3f;
	private static final float RESOURCE_TICK_TIME = 1f;
	
	private static final int WORKER_FOOD_COST = 2;
	private static final int WORKER_METAL_COST = 2;
	private static final int WORKER_WOOD_COST = 2;
	
	private static final int SOLDIER_FOOD_COST = 4;
	private static final int SOLDIER_METAL_COST = 4;
	private static final int SOLDIER_WOOD_COST = 4;
	
	private int cityFoodCost = 120;
	private int cityWoodCost = 230;
	private int cityMetalCost = 70;

	private final Array<PlayerHumanoid> pendingHumanoids = new Array<PlayerHumanoid>();
	
	private int maxPopulation = 1000;

	private final GameWorld.ResourceType type;
	
	private int food, metal, wood;
	private int workerCount = 10;
	
	private Tile centerTile;
	private GameWorld gameWorld;
	
	private Array<Tile> citySurroundingTiles;
	private Array<Troop> ownedTroops = new Array<Troop>();
	
	public City(GameWorld gameWorld, TextureRegion region, float x, float y, GameWorld.ResourceType type, Tile centralTile) {

		this.gameWorld = gameWorld;
		
		this.type = type;
		this.centerTile = centralTile;
		
		setTexture(region);
		setX(x);
		setY(y);
		setWidth(region.getRegionWidth());
		setHeight(region.getRegionHeight());
		
		for (int i = 0; i < 10; i += 1) {
			ownedTroops.insert(0, createTroop());
		}
	}

	public Array<Tile> getCitySurroundingTiles() {
		return citySurroundingTiles;
	}

	public void setCitySurroundingTiles(Array<Tile> citySurroundingTiles) {
		this.citySurroundingTiles = citySurroundingTiles;
	}

	public void upgradeCity() {
		if(canBuy(cityFoodCost, cityMetalCost, cityWoodCost)) {
			maxPopulation *= 2;
			
			food -= cityFoodCost;
			metal -= cityMetalCost;
			wood -= cityWoodCost;
			
			cityFoodCost *= 3;
			cityMetalCost *= 4;
			cityWoodCost *= 2;
		}
	}
	
	public void makeWorker() {
		Log.trace(type);
		if(workerCount + ownedTroops.size < maxPopulation 
		   && canBuy(WORKER_FOOD_COST, WORKER_METAL_COST, WORKER_WOOD_COST)) {
			workerCount++;
			food -= WORKER_FOOD_COST;
			metal -= WORKER_METAL_COST;
			wood -= WORKER_WOOD_COST;
		}
	}
	
	public void defendFrom (final BruteOrc orc) {
		if (ownedTroops.size == 0) {
			return;
		}
		
		final Troop troop = ownedTroops.pop();
		
		for (int ii = 0; ii < gameWorld.getCities().size; ii += 1) {
			if (gameWorld.getCities().get(ii) == this) {
				troop.setSourceCity(ii);
				troop.setDestinationCity(ii);
			}
		}

		troop.setState(Humanoid.State.IDLE);
		
		troop.setX(orc.getX() + orc.getWidth());
		troop.setY(orc.getY());
		
		troop.setSecondaryTarget(orc);
		
		gameWorld.getEntities().add(troop);
		
		orc.setSecondaryTarget(troop);
	}
	
	public void hit (final BruteOrc orc) {
		defendFrom(orc);
		
		final Decal decal = new Decal();
		decal.setTexture(gameWorld.getAssets().hit);
		decal.setWidth(gameWorld.getMap().getTileWidth());
		decal.setHeight(gameWorld.getMap().getTileHeight());
		decal.setX(MathUtils.random(getX(), getX() + getWidth() - decal.getWidth()));
		decal.setY(MathUtils.random(getY(), getY() + getHeight() - decal.getHeight()));
		decal.setSpeed(0.3f);
		gameWorld.getDecals().add(decal);
		
		if (workerCount == 0) {
			return;
		}
		
		workerCount -= 1;
	}
	
	public void sendWorkersTo(GameWorld gameWorld, City city, int count, boolean withResources) {
		if (city == this) {
			return;
		}
		
		this.setWorkerCount(getWorkerCount() - count);
		
		final Map map = gameWorld.getMap();
		final int cityX = (int)(map.getWidth() * (centerTile.getX() / (map.getWidth() * map.getTileWidth())));
		final int cityY = (int)(map.getHeight() * (centerTile.getY() / (map.getHeight() * map.getTileHeight())));
		final int dstX = (int)(map.getWidth() * (city.getCentralTile().getX() / (map.getWidth() * map.getTileWidth())));
		final int dstY = (int)(map.getHeight() * (city.getCentralTile().getY() / (map.getHeight() * map.getTileHeight())));
		
		for (int i = 0; i < count; i += 1) {
			final Worker worker = new Worker();
			worker.setTexture(gameWorld.getAssets().worker);
			worker.setWidth(map.getTileWidth());
			worker.setHeight(map.getTileHeight());
			worker.setPixelsPerSecond(map.getPixelsPerSecond());
			worker.setResourcesCarried(withResources ? RESOURCE_PER_WORKER : 0);
			worker.setType(type);
			
			for (int ii = 0; ii < gameWorld.getCities().size; ii += 1) {
				if (gameWorld.getCities().get(ii) == this) {
					worker.setSourceCity(ii);
				}
				else if (gameWorld.getCities().get(ii) == city) {
					worker.setDestinationCity(ii);
				}
			}

			worker.setX(centerTile.getX());
			worker.setY(centerTile.getY());
			worker.setLastPosition(cityX, cityY);
			worker.setDestination(dstX, dstY);
			worker.setState(Humanoid.State.WALKING);

			gameWorld.getMovableManager().setupRoadMovement(worker, cityX, cityY, dstX, dstY);
			pendingHumanoids.add(worker);
		}
		
		if (withResources) {
			switch (type) {
			case WOOD:
				wood -= count * RESOURCE_PER_WORKER;
				break;
			case IRON:
				metal -= count * RESOURCE_PER_WORKER;
				break;
			case FOOD:
				food -= count * RESOURCE_PER_WORKER;
				break;
			default:
				break;
			}
		}

	}
	
	private Troop createTroop () {
		final Troop troop = new Troop();
		troop.setTexture(gameWorld.getAssets().troop);
		troop.setWidth(gameWorld.getMap().getTileWidth());
		troop.setHeight(gameWorld.getMap().getTileHeight());
		troop.setState(Humanoid.State.IDLE);
		
		return troop;
	}
	
	public void makeSoldier() {
		if(workerCount + ownedTroops.size < maxPopulation 
		   && canBuy(SOLDIER_FOOD_COST, SOLDIER_METAL_COST, SOLDIER_WOOD_COST)) {
			food -= SOLDIER_FOOD_COST;
			metal -= SOLDIER_METAL_COST;
			wood -= SOLDIER_WOOD_COST;
			
			ownedTroops.insert(0, createTroop());
		}
	}
	
	public void addTroop (final Troop troop) {
		ownedTroops.add(troop);
	}
	
	public void sendSoldiersTo(GameWorld gameWorld, City city, int count) {
		if (city == this) {
			return;
		}
		
		final Map map = gameWorld.getMap();
		final int cityX = (int)(map.getWidth() * (centerTile.getX() / (map.getWidth() * map.getTileWidth())));
		final int cityY = (int)(map.getHeight() * (centerTile.getY() / (map.getHeight() * map.getTileHeight())));
		final int dstX = (int)(map.getWidth() * (city.getCentralTile().getX() / (map.getWidth() * map.getTileWidth())));
		final int dstY = (int)(map.getHeight() * (city.getCentralTile().getY() / (map.getHeight() * map.getTileHeight())));
		
		for (int i = 0; i < count; i += 1) {
			final Troop troop = ownedTroops.pop();
			troop.setPixelsPerSecond(map.getPixelsPerSecond());
			
			for (int ii = 0; ii < gameWorld.getCities().size; ii += 1) {
				if (gameWorld.getCities().get(ii) == this) {
					troop.setSourceCity(ii);
				}
				else if (gameWorld.getCities().get(ii) == city) {
					troop.setDestinationCity(ii);
				}
			}

			troop.setDestination(dstX, dstY);
			troop.setState(Humanoid.State.WALKING);
			
			gameWorld.getMovableManager().setupRoadMovement(troop, cityX, cityY, dstX, dstY);
			troop.setLastPosition(cityX, cityY);
			troop.setX(troop.getLastPositionX() * map.getTileWidth());
			troop.setY(troop.getLastPositonY() * map.getTileHeight());
			
			pendingHumanoids.add(troop);
		}
	}
	
	private boolean canBuy(float foodCost, float metalCost, float woodCost) {
		if(foodCost <= food) {
			if(metalCost <= metal) {
				if(woodCost <= wood) {
					return true;
				}
			}
		}
		return false;
	}
	
	private float resourceTime;
	private float foodTime;
	private float spawnTime;
	private float nextSpawnTime = MathUtils.random(0.2f, 0.4f);
	
	public void update(float delta) {
		resourceTime += delta;
		spawnTime += delta;
		
		if (spawnTime >= nextSpawnTime) {
			spawnTime = 0;
			nextSpawnTime = MathUtils.random(0.2f, 0.4f);
			
			if (pendingHumanoids.size > 0) {
				final PlayerHumanoid player = pendingHumanoids.first();
				pendingHumanoids.removeIndex(0);
				
				gameWorld.getEntities().add(player);
			}
		}
		
		if(resourceTime >= 1f / workerCount * 10) {
			resourceTime = 0;
			
			/*if(food > 0)
				food--;
			if(metal > 0)
				metal--;
			if(wood > 0)
				wood--;*/
			
			if(type == GameWorld.ResourceType.FOOD) {
				food += 1;//workerCount * 1;
			} else if(type == GameWorld.ResourceType.IRON) {
				metal += 1;//workerCount * 1;
			} else if(type == GameWorld.ResourceType.WOOD){
				wood += 1;//workerCount * 1;
			}
		}
		/*if(food <= 0) {
			foodTime += delta;
			if(foodTime >= PEOPLE_DIE_TIME) {
				if(workerCount > 0)
					workerCount--;
				if(soldierCount > 0)
					soldierCount--;
			}
		}*/
	}
	
	public GameWorld.ResourceType getType() {
		return type;
	}
	
	public Tile getCentralTile() {
		return centerTile;
	}

	public int getFoodCount() {
		return food;
	}

	public void setFoodCount(int food) {
		this.food = food;
	}

	public int getMetalCount() {
		return metal;
	}

	public void setMetalCount(int metal) {
		this.metal = metal;
	}

	public int getWoodCount() {
		return wood;
	}

	public void setWoodCount(int wood) {
		this.wood = wood;
	}

	public int getSoldierCount() {
		return ownedTroops.size;
	}

	public int getWorkerCount() {
		return workerCount;
	}

	public void setWorkerCount(int workerCount) {
		this.workerCount = workerCount;
	}

}

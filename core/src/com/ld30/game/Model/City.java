package com.ld30.game.Model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.utils.Log;

public class City extends Entity {
	public static final int RESOURCE_PER_WORKER = 5;
	
	private static final float PEOPLE_DIE_TIME = 3f;
	private static final float RESOURCE_TICK_TIME = 1f;
	
	private static final int WORKER_FOOD_COST = 20;
	private static final int WORKER_METAL_COST = 5;
	private static final int WORKER_WOOD_COST = 10;
	
	private static final int SOLDIER_FOOD_COST = 25;
	private static final int SOLDIER_METAL_COST = 40;
	private static final int SOLDIER_WOOD_COST = 15;
	
	private int cityFoodCost = 120;
	private int cityWoodCost = 230;
	private int cityMetalCost = 70;
	
	private int maxPopulation = 10;

	private final GameWorld.ResourceType type;
	
	private int food, metal, wood;
	private int soldierCount, workerCount;
	
	private Tile centerTile;
	
	public City(TextureRegion region, float x, float y, GameWorld.ResourceType type, Tile centralTile) {

		this.type = type;
		this.centerTile = centralTile;
		
		setTexture(region);
		setX(x);
		setY(y);
		setWidth(region.getRegionWidth());
		setHeight(region.getRegionHeight());
		
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
		if(workerCount + soldierCount < maxPopulation 
		   && canBuy(WORKER_FOOD_COST, WORKER_METAL_COST, WORKER_WOOD_COST)) {
			workerCount++;
			food -= WORKER_FOOD_COST;
			metal -= WORKER_METAL_COST;
			wood -= WORKER_WOOD_COST;
		}
	}
	
	public void sendWorkersTo(GameWorld gameWorld, City city, int count, boolean withResources) {
		if(count <= workerCount) {
			//TODO do stuff
		}
	}
	
	public void makeSoldier() {
		if(workerCount + soldierCount < maxPopulation 
		   && canBuy(SOLDIER_FOOD_COST, SOLDIER_METAL_COST, SOLDIER_WOOD_COST)) {
			soldierCount++;
			food -= SOLDIER_FOOD_COST;
			metal -= SOLDIER_METAL_COST;
			wood -= SOLDIER_WOOD_COST;
		}
	}
	
	public void sendSoldiersTo(GameWorld gameWorld, City city, int count) {
		if(count <= soldierCount) {
			//TODO do stuff
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
	public void update(float delta) {
		resourceTime += delta;
		
		if(resourceTime >= RESOURCE_TICK_TIME) {
			resourceTime = 0;
			
			/*if(food > 0)
				food--;
			if(metal > 0)
				metal--;*/
			if(wood > 0)
				wood--;
			
			if(type == GameWorld.ResourceType.FOOD) {
				food += workerCount;
			} else if(type == GameWorld.ResourceType.IRON) {
				metal += workerCount;
			} else if(type == GameWorld.ResourceType.WOOD){
				wood += workerCount;
			}
		}
		if(food <= 0) {
			foodTime += delta;
			if(foodTime >= PEOPLE_DIE_TIME) {
				if(workerCount > 0)
					workerCount--;
				if(soldierCount > 0)
					soldierCount--;
			}
		}
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
		return soldierCount;
	}

	public void setSoldierCount(int soldierCount) {
		this.soldierCount = soldierCount;
	}

	public int getWorkerCount() {
		return workerCount;
	}

	public void setWorkerCount(int workerCount) {
		this.workerCount = workerCount;
	}

}

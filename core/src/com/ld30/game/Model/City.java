package com.ld30.game.Model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class City extends Entity {
	private static final float PEOPLE_DIE_TIME = 3f;
	private static final float RESOURCE_TICK_TIME = 1f;
	
	private static final int WORKER_FOOD_COST = 20;
	private static final int WORKER_METAL_COST = 5;
	private static final int WORKER_WOOD_COST = 10;
	
	private static final int SOLDIER_FOOD_COST = 25;
	private static final int SOLDIER_METAL_COST = 40;
	private static final int SOLDIER_WOOD_COST = 15;
	
	public static enum Type{
		FOOD, METAL, WOOD;
	}
	private final Type type;
	
	private int food, metal, wood;
	private int soldierCount, workerCount;
	
	private TextureRegion region;
	private float x, y, width, height;
	
	public City(TextureRegion region, float x, float y, Type type) {
		this.type = type;
		
		setTexture(region);
		setX(x);
		setY(y);
		setWidth(region.getRegionWidth());
		setHeight(region.getRegionHeight());
		
	}
	
	public void makeWorker() {
		if(WORKER_FOOD_COST <= food) {
			if(WORKER_WOOD_COST <= wood) {
				if(WORKER_METAL_COST <= metal) {
					workerCount++;
				}
			}
		}
	}
	
	public void makeSoldier() {
		if(SOLDIER_METAL_COST <= metal) {
			if(SOLDIER_FOOD_COST <= food) {
				if(SOLDIER_WOOD_COST <= wood) {
					soldierCount++;
				}
			}
		}
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
				metal--;
			if(wood > 0)
				wood--;*/
			
			if(type == Type.FOOD) {
				food += workerCount;
			} else if(type == Type.METAL) {
				metal += workerCount;
			} else {
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

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}

	public int getMetal() {
		return metal;
	}

	public void setMetal(int metal) {
		this.metal = metal;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
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

	public TextureRegion getRegion() {
		return region;
	}

	public void setRegion(TextureRegion region) {
		this.region = region;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}

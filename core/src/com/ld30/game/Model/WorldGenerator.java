package com.ld30.game.Model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.ld30.game.Assets;
import com.ld30.game.Model.Tiles.Grass;
import com.ld30.game.Model.Tiles.Road;
import com.ld30.game.Model.Tiles.Rock;
import com.ld30.game.Model.Tiles.ShallowWater;
import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.Model.Tiles.Tree;
import com.ld30.game.Model.Tiles.Water;
import com.ld30.game.utils.AStar;

public class WorldGenerator {
	
	public static class GeneratedWorld {
		public final Tile[][] tiles;
		public final Tile[] centers;
		private Array<Tile> roadFromFoodToIron;
		private Array<Tile> roadFromIronToWood;
		private Array<Tile> roadWoodFoodToFood;
		
		public GeneratedWorld (final Tile[][] tiles, 
							   final Tile[] centers) {
			
			this.tiles = tiles;
			this.centers = centers;
		}

		public Array<Tile> getRoadFromFoodToIron() {
			return roadFromFoodToIron;
		}

		public void setRoadFromFoodToIron(Array<Tile> roadFromFoodToIron) {
			this.roadFromFoodToIron = roadFromFoodToIron;
		}

		public Array<Tile> getRoadFromIronToWood() {
			return roadFromIronToWood;
		}

		public void setRoadFromIronToWood(Array<Tile> roadFromIronToWood) {
			this.roadFromIronToWood = roadFromIronToWood;
		}

		public Array<Tile> getRoadWoodToFood() {
			return roadWoodFoodToFood;
		}

		public void setRoadWoodFoodToFood(Array<Tile> roadWoodFoodToFood) {
			this.roadWoodFoodToFood = roadWoodFoodToFood;
		}
		
	}
	
	public static GeneratedWorld generateMap(Assets assets, float tWH, AStar astar, int mapWidth, int mapHeight) {
		Tile[][] tiles = new Tile[mapWidth][mapHeight];
		
		/*
		 * Fill tile array with grass
		 */
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				Tile t = new Grass(assets.grass, x*tWH, y*tWH);
				tiles[x][y] = t;
			}
		}
		
		/*
		 * Create river start and end points
		 */
		int riverOffset = (int) (tiles[0].length*0.15f);
		Vector2 riverStart = new Vector2(0f, (float) Math.floor(Math.random()*(tiles[0].length-2*riverOffset))+riverOffset);
		Water r1 = new Water(assets.water, tWH*riverStart.x, tWH*riverStart.y);
		tiles[(int)riverStart.x][(int)riverStart.y] = r1;
		Vector2 riverEnd = new Vector2(tiles.length-1, (float) Math.floor(Math.random()*(tiles[0].length-2*riverOffset))+riverOffset);
		Water r2 = new Water(assets.water, tWH*riverEnd.x, tWH*riverEnd.y);
		tiles[(int)riverEnd.x][(int)riverEnd.y] = r2;
		
		float currentX = riverStart.x;
		float currentY = riverStart.y;
		if(tiles[(int)currentX][(int)currentY-1] instanceof Grass) {
			Water w = new Water(assets.water, tWH*currentX, tWH*(currentY-1));
			tiles[(int)currentX][(int)currentY-1] = w;
		} else if(tiles[(int)currentX][(int)currentY-1] instanceof Road) {
			Road r = (Road) tiles[(int)currentX][(int)currentY-1];
			if(r.getCenter() == GameWorld.ResourceType.NONE) {
				Water w = new Water(assets.water, tWH*currentX, tWH*(currentY-1));
				tiles[(int)currentX][(int)currentY-1] = w;
			}
		}
		if(tiles[(int)currentX][(int)currentY+1] instanceof Grass) {
			Water w = new Water(assets.water, tWH*currentX, tWH*(currentY+1));
			tiles[(int)currentX][(int)currentY+1] = w;
		} else if(tiles[(int)currentX][(int)currentY+1] instanceof Road) {
			Road r = (Road) tiles[(int)currentX][(int)currentY+1];
			if(r.getCenter() == GameWorld.ResourceType.NONE) {
				Water w = new Water(assets.water, tWH*currentX, tWH*(currentY+1));
				tiles[(int)currentX][(int)currentY+1] = w;
			}
		}
		float endX = riverEnd.x;
		float endY = riverEnd.y;
		if(tiles[(int)endX][(int)endY-1] instanceof Grass) {
			Water w = new Water(assets.water, tWH*endX, tWH*(endY-1));
			tiles[(int)endX][(int)endY-1] = w;
		} else if(tiles[(int)endX][(int)endY-1] instanceof Road) {
			Road r = (Road) tiles[(int)endX][(int)endY-1];
			if(r.getCenter() == GameWorld.ResourceType.NONE) {
				Water w = new Water(assets.water, tWH*endX, tWH*(endY-1));
				tiles[(int)endX][(int)endY-1] = w;
			}
		}
		if(tiles[(int)endX][(int)currentY+1] instanceof Grass) {
			Water w = new Water(assets.water, tWH*endX, tWH*(endY+1));
			tiles[(int)endX][(int)endY+1] = w;
		} else if(tiles[(int)endX][(int)endY+1] instanceof Road) {
			Road r = (Road) tiles[(int)endX][(int)endY+1];
			if(r.getCenter() == GameWorld.ResourceType.NONE) {
				Water w = new Water(assets.water, tWH*endX, tWH*(endY+1));
				tiles[(int)endX][(int)endY+1] = w;
			}
		}
		
		/*
		 * Create city centres
		 */
		Road woodCityCentre = null;
		Road ironCityCentre = null;
		Road foodCityCentre = null;
		Array<String> cityNames = new Array<String>();
		cityNames.add("food"); cityNames.add("iron"); cityNames.add("wood");
		cityNames.shuffle();
		int offset = 5;  //Must not be <1
		int count = 0;
		for(String name : cityNames) {
			count++;
			float randomX = 0;
			float randomY = 0;
			if(count == 1) {
				randomX = (float) Math.floor(Math.random()*(Math.floor(tiles.length/3)))+offset;
				randomY = (float) Math.floor(Math.random()*(tiles[0].length/3))+offset;
			} else if(count == 2) {
				randomX = (float) Math.floor(Math.random()*(Math.floor(tiles.length/3)))+tiles.length/3*2-offset;
				randomY = (float) Math.floor(Math.random()*(tiles[0].length/3))+offset;
			} else if(count == 3) {
				randomX = (float) Math.floor(Math.random()*(tiles.length-2*offset))+offset;
				randomY = (float) Math.floor(Math.random()*(Math.floor(tiles[0].length/2)))+tiles[0].length/2-offset;
			}
			
			if(name.equals("food")) {
				foodCityCentre = new Road(assets.road, randomX*tWH, randomY*tWH);
				foodCityCentre.setCenter(GameWorld.ResourceType.FOOD);
				tiles[(int)randomX][(int)randomY] = foodCityCentre;
			} else if(name.equals("iron")) {
				ironCityCentre = new Road(assets.road, randomX*tWH, randomY*tWH);
				ironCityCentre.setCenter(GameWorld.ResourceType.IRON);
				tiles[(int)randomX][(int)randomY] = ironCityCentre;
			}
			else if(name.equals("wood")) {
				woodCityCentre = new Road(assets.road, randomX*tWH, randomY*tWH);
				woodCityCentre.setCenter(GameWorld.ResourceType.WOOD);
				tiles[(int)randomX][(int)randomY] = woodCityCentre;
			}
		}
		
		final Tile[] centers = new Tile[] {
				woodCityCentre,
				ironCityCentre,
				foodCityCentre
		};
		
		/*
		 * Create various objects in the map
		 */
		addRocksToMap(tiles, assets, tWH);
		addTreesToMap(tiles, assets, tWH);
		Array<Tile> river = addRiverToMap(astar, tiles, assets, tWH, riverStart, riverEnd, mapWidth, mapHeight);
		
		/*
		 * Create roads between city centres
		 */
		Array<Tile> roadFromFoodToIron = addRoadFromFoodToIron(foodCityCentre, ironCityCentre, tiles, assets, astar, tWH);
		Array<Tile> roadFromIronToWood = addRoadFromIronToWood(ironCityCentre, woodCityCentre, tiles, assets, astar, tWH);
		Array<Tile> roadFromWoodToFood = addRoadFromWoodToFood(woodCityCentre, foodCityCentre, tiles, assets, astar, tWH);
		
		GeneratedWorld generatedWorld = new GeneratedWorld(tiles, centers);
		generatedWorld.setRoadFromFoodToIron(roadFromFoodToIron);
		generatedWorld.setRoadFromIronToWood(roadFromIronToWood);
		generatedWorld.setRoadWoodFoodToFood(roadFromWoodToFood);
		
		/*
		 * Transform map
		 */
		//Add shallow waters
		int r = MathUtils.random(2)+2;
		for(int i = 0; i < r; i++) {
			addShallowWater(tiles, river, assets, tWH);
		}
		//Transform cities' surroundings according to their type
		transformCitiesSurroundings(tiles, centers, cityNames, assets, tWH);
		
		return generatedWorld;
	}
	
	private static void transformCitiesSurroundings(Tile[][] tiles, Tile[] centers, Array<String> cityNames, Assets assets, float tWH) {
		//XXX: variables with warnings are needed for gradient generation, which is now turned off
		float radiusInTiles = 14;
		float percentageIncrement = (float) Math.ceil(100/radiusInTiles);
		float chance = 0;
		float objectSpawnChance = 0.13f;
		Road center = null;
		for(String name : cityNames) {
			if(name.equals("food")) {
				center = (Road) centers[2];
			} else if(name.equals("wood")) {
				center = (Road) centers[0];
			} else if(name.equals("iron")) {
				center = (Road) centers[1];
			}
			
			for(int x = 0; x < tiles.length; x++) {
				for(int y = 0; y < tiles[0].length; y++) {
					float dx = x-center.getX()/tWH;
					float dy = y-center.getY()/tWH;
					float dist = (float) Math.floor(Math.sqrt(dx*dx+dy*dy));
					
					if(center.getCenter() == GameWorld.ResourceType.FOOD) {
						if(tiles[x][y] instanceof Tree || tiles[x][y] instanceof Rock) {
							float foodRadiusInTiles = radiusInTiles + radiusInTiles/3;
							float foodPercentageIncrement = (float) Math.ceil(100/foodRadiusInTiles);
							if(dist <= foodRadiusInTiles) {
								//roll, and see if swap is possible
								//XXX: comment out theese comments to turn on gradient
								//chance = (float) Math.random()*100;
								//if(chance >= dist*foodPercentageIncrement-foodPercentageIncrement) {
									Tile t = new Grass(assets.grass, x*tWH, y*tWH);
									tiles[x][y] = t;
								//}
							}
						}
					} else if(center.getCenter() == GameWorld.ResourceType.IRON) {
						if(tiles[x][y] instanceof Tree) {
							if(dist <= radiusInTiles) {
								//roll, and see if swap is possible
								//XXX: comment out theese comments to turn on gradient
								//chance = (float) Math.random()*100;
								//if(chance >= dist*percentageIncrement-percentageIncrement) {
									Tile t = new Rock(assets.grass, x*tWH, y*tWH);
									tiles[x][y] = t;
								//}
							}
						} else if(tiles[x][y] instanceof Grass) {
							float r = (float) Math.random();
							if(r < objectSpawnChance) {
								Tile t = new Rock(assets.grass, x*tWH, y*tWH);
								tiles[x][y] = t;
							}
						}
					} else if(center.getCenter() == GameWorld.ResourceType.WOOD) {
						if(tiles[x][y] instanceof Rock) {
							if(dist <= radiusInTiles) {
								//roll, and see if swap is possible
								//XXX: comment out theese comments to turn on gradient
								//chance = (float) Math.random()*100;
								//if(chance >= dist*percentageIncrement-percentageIncrement) {
									Tile t = new Tree(assets.grass, x*tWH, y*tWH);
									tiles[x][y] = t;
								//}
							}
						} else if(tiles[x][y] instanceof Grass) {
							float r = (float) Math.random();
							if(r < objectSpawnChance) {
								Tile t = new Tree(assets.grass, x*tWH, y*tWH);
								tiles[x][y] = t;
							}
						}
					}
				}
			}
		}
	}
	
	private static void addShallowWater(Tile[][] tiles, Array<Tile> river, Assets assets, float tWH) {
		int r = MathUtils.random(river.size-1);
		float currentX = river.get(r).getX()/tWH;
		float currentY = river.get(r).getY()/tWH;
		if(currentX < 1) currentX = 1; else if(currentX >= tiles.length-1) currentX = tiles.length-2;
		if(currentY < 1) currentY = 1; else if(currentY >= tiles[0].length-1) currentY = tiles[0].length-2;
		ShallowWater tile = new ShallowWater(assets.shallowWater, tWH*currentX, tWH*currentY);
		tiles[(int)currentX][(int)currentY] = tile;
		
		if(tiles[(int)currentX-1][(int)currentY] instanceof Water) {
			ShallowWater t = new ShallowWater(assets.shallowWater, tWH*(currentX-1), tWH*(currentY));
			tiles[(int)currentX-1][(int)currentY] = t;
		}
		if(tiles[(int)currentX-1][(int)currentY+1] instanceof Water) {
			ShallowWater t = new ShallowWater(assets.shallowWater, tWH*(currentX-1), tWH*(currentY+1));
			tiles[(int)currentX-1][(int)currentY+1] = t;
		}
		if(tiles[(int)currentX][(int)currentY+1] instanceof Water) {
			ShallowWater t = new ShallowWater(assets.shallowWater, tWH*(currentX), tWH*(currentY+1));
			tiles[(int)currentX][(int)currentY+1] = t;
		}
		if(tiles[(int)currentX+1][(int)currentY+1] instanceof Water) {
			ShallowWater t = new ShallowWater(assets.shallowWater, tWH*(currentX+1), tWH*(currentY+1));
			tiles[(int)currentX+1][(int)currentY+1] = t;
		}
		if(tiles[(int)currentX+1][(int)currentY] instanceof Water) {
			ShallowWater t = new ShallowWater(assets.shallowWater, tWH*(currentX+1), tWH*(currentY));
			tiles[(int)currentX+1][(int)currentY] = t;
		}
		if(tiles[(int)currentX+1][(int)currentY-1] instanceof Water) {
			ShallowWater t = new ShallowWater(assets.shallowWater, tWH*(currentX+1), tWH*(currentY-1));
			tiles[(int)currentX+1][(int)currentY-1] = t;
		}
		if(tiles[(int)currentX][(int)currentY-1] instanceof Water) {
			ShallowWater t = new ShallowWater(assets.shallowWater, tWH*(currentX), tWH*(currentY-1));
			tiles[(int)currentX][(int)currentY-1] = t;
		}
		if(tiles[(int)currentX-1][(int)currentY-1] instanceof Water) {
			ShallowWater t = new ShallowWater(assets.shallowWater, tWH*(currentX-1), tWH*(currentY-1));
			tiles[(int)currentX-1][(int)currentY-1] = t;
		}
	}
	
	private static Array<Tile> addRoadFromFoodToIron(Road foodCityCentre, Road ironCityCentre, final Tile[][] tiles, Assets assets, AStar astar, float tWH) {
		Array<Tile> result = new Array<Tile>();
		AStar.Validator roadAStarValidation = new AStar.Validator() {
			@Override
			public boolean isValid(int x, int y) {
				return (tiles[x][y] instanceof Grass || tiles[x][y] instanceof Water || tiles[x][y] instanceof Road);
			}
		};
		IntArray path = new IntArray();
		path.addAll(astar.getPath((int)(foodCityCentre.getX()/tWH), (int)(foodCityCentre.getY()/tWH), (int)(ironCityCentre.getX()/tWH), (int)(ironCityCentre.getY()/tWH), roadAStarValidation));
		
		for(int i = 0; i < path.size; i+=2) {
			float currentX = path.get(i);
			float currentY = path.get(i+1);
			if(tiles[(int)currentX][(int)currentY] instanceof Water) {
				ShallowWater tile = new ShallowWater(assets.shallowWater, tWH*currentX, tWH*currentY);
				tiles[(int)currentX][(int)currentY] = tile;
				result.add(tile);
			} else {
				Road tile = new Road(assets.road, tWH*currentX, tWH*currentY);
				tiles[(int)currentX][(int)currentY] = tile;
				tile.setCenter(GameWorld.ResourceType.NONE);
				result.add(tile);
			}
		}
		
		return result;
	}
	
	private static Array<Tile> addRoadFromIronToWood(Road ironCityCentre, Road woodCityCentre, final Tile[][] tiles, Assets assets, AStar astar, float tWH) {
		Array<Tile> result = new Array<Tile>();
		AStar.Validator roadAStarValidation = new AStar.Validator() {
			@Override
			public boolean isValid(int x, int y) {
				return (tiles[x][y] instanceof Grass || tiles[x][y] instanceof Water || tiles[x][y] instanceof Road);
			}
		};
		IntArray path = new IntArray();
		path.addAll(astar.getPath((int)(ironCityCentre.getX()/tWH), (int)(ironCityCentre.getY()/tWH), (int)(woodCityCentre.getX()/tWH), (int)(woodCityCentre.getY()/tWH), roadAStarValidation));
		
		for(int i = 0; i < path.size; i+=2) {
			float currentX = path.get(i);
			float currentY = path.get(i+1);
			if(tiles[(int)currentX][(int)currentY] instanceof Water) {
				ShallowWater tile = new ShallowWater(assets.shallowWater, tWH*currentX, tWH*currentY);
				tiles[(int)currentX][(int)currentY] = tile;
				result.add(tile);
			} else {
				Road tile = new Road(assets.road, tWH*currentX, tWH*currentY);
				tiles[(int)currentX][(int)currentY] = tile;
				tile.setCenter(GameWorld.ResourceType.NONE);
				result.add(tile);
			}
		}
		
		return result;
	}
	
	private static Array<Tile> addRoadFromWoodToFood(Road woodCityCentre, Road foodCityCentre, final Tile[][] tiles, Assets assets, AStar astar, float tWH) {
		Array<Tile> result = new Array<Tile>();
		AStar.Validator roadAStarValidation = new AStar.Validator() {
			@Override
			public boolean isValid(int x, int y) {
				return (tiles[x][y] instanceof Grass || tiles[x][y] instanceof Water || tiles[x][y] instanceof Road);
			}
		};
		IntArray path = new IntArray();
		path.addAll(astar.getPath((int)(woodCityCentre.getX()/tWH), (int)(woodCityCentre.getY()/tWH), (int)(foodCityCentre.getX()/tWH), (int)(foodCityCentre.getY()/tWH), roadAStarValidation));
		
		for(int i = 0; i < path.size; i+=2) {
			float currentX = path.get(i);
			float currentY = path.get(i+1);
			if(tiles[(int)currentX][(int)currentY] instanceof Water) {
				ShallowWater tile = new ShallowWater(assets.shallowWater, tWH*currentX, tWH*currentY);
				tiles[(int)currentX][(int)currentY] = tile;
				result.add(tile);
			} else {
				Road tile = new Road(assets.road, tWH*currentX, tWH*currentY);
				tiles[(int)currentX][(int)currentY] = tile;
				tile.setCenter(GameWorld.ResourceType.NONE);
				result.add(tile);
			}
		}
		
		return result;
	}
	
	private static Array<Tile> addRiverToMap(AStar astar, final Tile[][] tiles, Assets assets, float tWH, Vector2 riverStart, Vector2 riverEnd, int mapW, int mapH) {
		Array<Tile> result = new Array<Tile>();
		astar.setSize(mapW, mapH);
		AStar.Validator riverAStarValidation = new AStar.Validator() {
			@Override
			public boolean isValid(int x, int y) {
				return (tiles[x][y] instanceof Grass || tiles[x][y] instanceof Water);
			}
		};
		IntArray path = new IntArray();
		path.addAll(astar.getPath((int)riverStart.x, (int)riverStart.y, (int)riverEnd.x, (int)riverEnd.y, riverAStarValidation));
		
		for(int i = 0; i < path.size; i+=2) {
			Water t = new Water(assets.water, tWH*path.get(i), tWH*path.get(i+1));
			result.add(t);
			tiles[path.get(i)][path.get(i+1)] = t;
			//Make river wider
			float currentX = path.get(i);
			float currentY = path.get(i+1);
			if(currentY-1 < 0) currentY = 1; else if (currentY > tiles[0].length-1) currentY = tiles[0].length-1;
			if(tiles[(int)currentX][(int)currentY-1] instanceof Grass) {
				Water w = new Water(assets.water, tWH*currentX, tWH*(currentY-1));
				tiles[(int)currentX][(int)currentY-1] = w;
			} else if(tiles[(int)currentX][(int)currentY-1] instanceof Road) {
				Road r = (Road) tiles[(int)currentX][(int)currentY-1];
				if(r.getCenter() == GameWorld.ResourceType.NONE) {
					Water w = new Water(assets.water, tWH*currentX, tWH*(currentY-1));
					tiles[(int)currentX][(int)currentY-1] = w;
				}
			}
			if(tiles[(int)currentX][(int)currentY+1] instanceof Grass) {
				Water w = new Water(assets.water, tWH*currentX, tWH*(currentY+1));
				tiles[(int)currentX][(int)currentY+1] = w;
			} else if(tiles[(int)currentX][(int)currentY+1] instanceof Road) {
				Road r = (Road) tiles[(int)currentX][(int)currentY+1];
				if(r.getCenter() == GameWorld.ResourceType.NONE) {
					Water w = new Water(assets.water, tWH*currentX, tWH*(currentY+1));
					tiles[(int)currentX][(int)currentY+1] = w;
				}
			}
		}
		
		return result;
	}
	
	private static Tile[][] addRocksToMap(Tile[][] tiles, Assets assets, float tWH) {
		//Scatter some rocks randomly on the map
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				float chance = MathUtils.random();
				if(chance < 0.13) {
					if(tiles[x][y] instanceof Grass) {
						//spawn rock
						Rock r = new Rock(assets.rock, x*tWH, y*tWH);
						tiles[x][y] = r;
					}
				}
			}
		}
		
		return tiles;
	}
	
	private static Tile[][] addTreesToMap(Tile[][] tiles, Assets assets, float tWH) {
		//Scatter some trees randomly on the map
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				float chance = MathUtils.random();
				if(chance < 0.13) {
					if(tiles[x][y] instanceof Grass) {
						//spawn rock
						Tree r = new Tree(assets.tree, x*tWH, y*tWH);
						tiles[x][y] = r;
					}
				}
			}
		}
		
		return tiles;
	}
}

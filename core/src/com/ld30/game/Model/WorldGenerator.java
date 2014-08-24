package com.ld30.game.Model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;
import com.ld30.game.Assets;
import com.ld30.game.Model.Tiles.Grass;
import com.ld30.game.Model.Tiles.Road;
import com.ld30.game.Model.Tiles.Rock;
import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.Model.Tiles.Tree;
import com.ld30.game.Model.Tiles.Water;
import com.ld30.game.utils.AStar;

public class WorldGenerator {
	
	public static class GeneratedWorld {
		public final Tile[][] tiles;
		public final Tile[] centers;
		
		public GeneratedWorld (final Tile[][] tiles, 
							   final Tile[] centers) {
			
			this.tiles = tiles;
			this.centers = centers;
		}
	}
	
	public static GeneratedWorld generateMap(Assets assets, float tWH, AStar astar) {
		int mapWidth = 64;
		int mapHeight = 64;
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
		Vector2 riverStart = new Vector2(0f, (float) Math.floor(Math.random()*(tiles[0].length-2))+1);
		Water r1 = new Water(assets.water, tWH*riverStart.x, tWH*riverStart.y);
		tiles[(int)riverStart.x][(int)riverStart.y] = r1;
		Vector2 riverEnd = new Vector2(tiles.length-1, (float) Math.floor(Math.random()*(tiles[0].length-2))+1);
		Water r2 = new Water(assets.water, tWH*riverEnd.x, tWH*riverEnd.y);
		tiles[(int)riverEnd.x][(int)riverEnd.y] = r2;
		
		float currentX = riverStart.x;
		float currentY = riverStart.y;
		if(tiles[(int)currentX][(int)currentY-1] instanceof Grass) {
			Water w = new Water(assets.water, tWH*currentX, tWH*(currentY-1));
			tiles[(int)currentX][(int)currentY-1] = w;
		} else if(tiles[(int)currentX][(int)currentY-1] instanceof Road) {
			Road r = (Road) tiles[(int)currentX][(int)currentY-1];
			if(r.getCenter() == GameWorld.Center.NONE) {
				Water w = new Water(assets.water, tWH*currentX, tWH*(currentY-1));
				tiles[(int)currentX][(int)currentY-1] = w;
			}
		}
		if(tiles[(int)currentX][(int)currentY+1] instanceof Grass) {
			Water w = new Water(assets.water, tWH*currentX, tWH*(currentY+1));
			tiles[(int)currentX][(int)currentY+1] = w;
		} else if(tiles[(int)currentX][(int)currentY+1] instanceof Road) {
			Road r = (Road) tiles[(int)currentX][(int)currentY+1];
			if(r.getCenter() == GameWorld.Center.NONE) {
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
			if(r.getCenter() == GameWorld.Center.NONE) {
				Water w = new Water(assets.water, tWH*endX, tWH*(endY-1));
				tiles[(int)endX][(int)endY-1] = w;
			}
		}
		if(tiles[(int)endX][(int)currentY+1] instanceof Grass) {
			Water w = new Water(assets.water, tWH*endX, tWH*(endY+1));
			tiles[(int)endX][(int)endY+1] = w;
		} else if(tiles[(int)endX][(int)endY+1] instanceof Road) {
			Road r = (Road) tiles[(int)endX][(int)endY+1];
			if(r.getCenter() == GameWorld.Center.NONE) {
				Water w = new Water(assets.water, tWH*endX, tWH*(endY+1));
				tiles[(int)endX][(int)endY+1] = w;
			}
		}
		
		/*
		 * Create city centres
		 */
		Road woodCityCentre, ironCityCentre, foodCityCentre;
		float randomX = (float) Math.floor(Math.random()*(Math.floor(tiles.length/3)))+1;
		float randomY = (float) Math.floor(Math.random()*(tiles[0].length/3))+1;
		foodCityCentre = new Road(assets.road, randomX*tWH, randomY*tWH);
		foodCityCentre.setCenter(GameWorld.Center.FOOD);
		
		tiles[(int)randomX][(int)randomY] = foodCityCentre;
		
		randomX = (float) Math.floor(Math.random()*(Math.floor(tiles.length/3)))+tiles.length/3*2-1;
		randomY = (float) Math.floor(Math.random()*(tiles[0].length/3))+1;
		ironCityCentre = new Road(assets.road, randomX*tWH, randomY*tWH);
		ironCityCentre.setCenter(GameWorld.Center.IRON);
		tiles[(int)randomX][(int)randomY] = ironCityCentre;
		
		randomX = (float) Math.floor(Math.random()*(tiles.length-2))+1;
		randomY = (float) Math.floor(Math.random()*(Math.floor(tiles[0].length/2)))+tiles[0].length/2-1;
		woodCityCentre = new Road(assets.road, randomX*tWH, randomY*tWH);
		woodCityCentre.setCenter(GameWorld.Center.WOOD);
		tiles[(int)randomX][(int)randomY] = woodCityCentre;
		
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
		addRiverToMap(astar, tiles, assets, tWH, riverStart, riverEnd, mapWidth, mapHeight);
		
		/*
		 * Create roads between city centres
		 */
		addRoadFromFoodToIron(foodCityCentre, ironCityCentre, tiles, assets, astar, tWH);
		//From food centre to iron
		/*currentX = foodCityCentre.getX()/tWH;
		currentY = foodCityCentre.getY()/tWH;
		float targetX = ironCityCentre.getX()/tWH;
		float targetY = ironCityCentre.getY()/tWH;
		while(currentX != targetX || currentY != targetY) {
			if(currentX < targetX) {
				currentX++;
			} else if(currentX > targetX) {
				currentX--;
			}
			
			if(currentY < targetY) {
				currentY++;
			} else if(currentY > targetY) {
				currentY--;
			}
			
			if(currentX == targetX && currentY == targetY) continue;
			
			if(tiles[(int)currentX][(int)currentY] instanceof Water) {
				ShallowWater tile = new ShallowWater(assets.shallowWater, tWH*currentX, tWH*currentY);
				tiles[(int)currentX][(int)currentY] = tile;
			} else {
				Road tile = new Road(assets.road, tWH*currentX, tWH*currentY);
				tiles[(int)currentX][(int)currentY] = tile;
				tile.setCenter(GameWorld.Center.NONE);
			}
			
			if(tiles[(int)currentX-1][(int)currentY] instanceof Grass) {
				Road t = new Road(assets.road, tWH*(currentX-1), tWH*currentY);
				t.setCenter(GameWorld.Center.NONE);
				tiles[(int)currentX-1][(int)currentY] = t;
			}
			if(tiles[(int)currentX+1][(int)currentY] instanceof Grass) {
				Road t = new Road(assets.road, tWH*(currentX+1), tWH*currentY);
				t.setCenter(GameWorld.Center.NONE);
				tiles[(int)currentX+1][(int)currentY] = t;
			}
			if(tiles[(int)currentX][(int)currentY-1] instanceof Grass) {
				Road t = new Road(assets.road, tWH*currentX, tWH*(currentY-1));
				t.setCenter(GameWorld.Center.NONE);
				tiles[(int)currentX][(int)currentY-1] = t;
			}
			if(tiles[(int)currentX][(int)currentY+1] instanceof Grass) {
				Road t = new Road(assets.road, tWH*currentX, tWH*(currentY+1));
				t.setCenter(GameWorld.Center.NONE);
				tiles[(int)currentX][(int)currentY+1] = t;
			}
		}*/
		//From iron city centre to wood
		/*currentX = ironCityCentre.getX()/tWH;
		currentY = ironCityCentre.getY()/tWH;
		targetX = woodCityCentre.getX()/tWH;
		targetY = woodCityCentre.getY()/tWH;
		while(currentX != targetX || currentY != targetY) {
			if(currentX < targetX) {
				currentX++;
			} else if(currentX > targetX) {
				currentX--;
			}
			
			if(currentY < targetY) {
				currentY++;
			} else if(currentY > targetY) {
				currentY--;
			}
			
			if(currentX == targetX && currentY == targetY) continue;
			
			if(tiles[(int)currentX][(int)currentY] instanceof Water) {
				ShallowWater tile = new ShallowWater(assets.shallowWater, tWH*currentX, tWH*currentY);
				tiles[(int)currentX][(int)currentY] = tile;
			} else {
				Road tile = new Road(assets.road, tWH*currentX, tWH*currentY);
				tiles[(int)currentX][(int)currentY] = tile;
				tile.setCenter(GameWorld.Center.NONE);
			}
			
			if(tiles[(int)currentX-1][(int)currentY] instanceof Grass) {
				Road t = new Road(assets.road, tWH*(currentX-1), tWH*currentY);
				t.setCenter(GameWorld.Center.NONE);
				tiles[(int)currentX-1][(int)currentY] = t;
			}
			if(tiles[(int)currentX+1][(int)currentY] instanceof Grass) {
				Road t = new Road(assets.road, tWH*(currentX+1), tWH*currentY);
				t.setCenter(GameWorld.Center.NONE);
				tiles[(int)currentX+1][(int)currentY] = t;
			}
			if(tiles[(int)currentX][(int)currentY-1] instanceof Grass) {
				Road t = new Road(assets.road, tWH*currentX, tWH*(currentY-1));
				t.setCenter(GameWorld.Center.NONE);
				tiles[(int)currentX][(int)currentY-1] = t;
			}
			if(tiles[(int)currentX][(int)currentY+1] instanceof Grass) {
				Road t = new Road(assets.road, tWH*currentX, tWH*(currentY+1));
				t.setCenter(GameWorld.Center.NONE);
				tiles[(int)currentX][(int)currentY+1] = t;
			}
		}*/
		//From wood city centre to food
		/*currentX = woodCityCentre.getX()/tWH;
		currentY = woodCityCentre.getY()/tWH;
		targetX = foodCityCentre.getX()/tWH;
		targetY = foodCityCentre.getY()/tWH;
		while(currentX != targetX || currentY != targetY) {
			if(currentX < targetX) {
				currentX++;
			} else if(currentX > targetX) {
				currentX--;
			}
			
			if(currentY < targetY) {
				currentY++;
			} else if(currentY > targetY) {
				currentY--;
			}
			
			if(currentX == targetX && currentY == targetY) continue;
			
			if(tiles[(int)currentX][(int)currentY] instanceof Water) {
				ShallowWater tile = new ShallowWater(assets.shallowWater, tWH*currentX, tWH*currentY);
				tiles[(int)currentX][(int)currentY] = tile;
			} else {
				Road tile = new Road(assets.road, tWH*currentX, tWH*currentY);
				tiles[(int)currentX][(int)currentY] = tile;
				tile.setCenter(GameWorld.Center.NONE);
			}
			
			if(tiles[(int)currentX-1][(int)currentY] instanceof Grass) {
				Road t = new Road(assets.road, tWH*(currentX-1), tWH*currentY);
				t.setCenter(GameWorld.Center.NONE);
				tiles[(int)currentX-1][(int)currentY] = t;
			}
			if(tiles[(int)currentX+1][(int)currentY] instanceof Grass) {
				Road t = new Road(assets.road, tWH*(currentX+1), tWH*currentY);
				t.setCenter(GameWorld.Center.NONE);
				tiles[(int)currentX+1][(int)currentY] = t;
			}
			if(tiles[(int)currentX][(int)currentY-1] instanceof Grass) {
				Road t = new Road(assets.road, tWH*currentX, tWH*(currentY-1));
				t.setCenter(GameWorld.Center.NONE);
				tiles[(int)currentX][(int)currentY-1] = t;
			}
			if(tiles[(int)currentX][(int)currentY+1] instanceof Grass) {
				Road t = new Road(assets.road, tWH*currentX, tWH*(currentY+1));
				t.setCenter(GameWorld.Center.NONE);
				tiles[(int)currentX][(int)currentY+1] = t;
			}
		}*/
		
		return new GeneratedWorld(tiles, centers);
	}
	
	private static Tile[] addRoadFromFoodToIron(Road foodCityCentre, Road ironCityCentre, Tile[][] tiles, Assets assets, AStar astar, float tWH) {
		Tile[] result;
		
		return null;
	}
	
	private static Tile[][] addRiverToMap(AStar astar, final Tile[][] tiles, Assets assets, float tWH, Vector2 riverStart, Vector2 riverEnd, int mapW, int mapH) {
		astar.setSize(mapW, mapH);
		AStar.Validator riverAStarValidation = new AStar.Validator() {
			@Override
			public boolean isValid(int x, int y) {
				return (tiles[x][y] instanceof Grass);
			}
		};
		riverEnd.x -= 1;
		IntArray path = new IntArray();
		path.addAll(astar.getPath((int)riverStart.x, (int)riverStart.y, (int)riverEnd.x, (int)riverEnd.y, riverAStarValidation));
		
		for(int i = 0; i < path.size; i+=2) {
			Water t = new Water(assets.water, tWH*path.get(i), tWH*path.get(i+1));
			tiles[path.get(i)][path.get(i+1)] = t;
			//Make river wider
			float currentX = path.get(i);
			float currentY = path.get(i+1);
			if(tiles[(int)currentX][(int)currentY-1] instanceof Grass) {
				Water w = new Water(assets.water, tWH*currentX, tWH*(currentY-1));
				tiles[(int)currentX][(int)currentY-1] = w;
			} else if(tiles[(int)currentX][(int)currentY-1] instanceof Road) {
				Road r = (Road) tiles[(int)currentX][(int)currentY-1];
				if(r.getCenter() == GameWorld.Center.NONE) {
					Water w = new Water(assets.water, tWH*currentX, tWH*(currentY-1));
					tiles[(int)currentX][(int)currentY-1] = w;
				}
			}
			if(tiles[(int)currentX][(int)currentY+1] instanceof Grass) {
				Water w = new Water(assets.water, tWH*currentX, tWH*(currentY+1));
				tiles[(int)currentX][(int)currentY+1] = w;
			} else if(tiles[(int)currentX][(int)currentY+1] instanceof Road) {
				Road r = (Road) tiles[(int)currentX][(int)currentY+1];
				if(r.getCenter() == GameWorld.Center.NONE) {
					Water w = new Water(assets.water, tWH*currentX, tWH*(currentY+1));
					tiles[(int)currentX][(int)currentY+1] = w;
				}
			}
		}
		
		return tiles;
	}
	
	private static Tile[][] addRocksToMap(Tile[][] tiles, Assets assets, float tWH) {
		//Scatter some rocks randomly on the map
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles.length; y++) {
				float chance = MathUtils.random();
				if(chance < 0.013) {
				//if(chance < 0.2) {
					if(tiles[x][y] instanceof Grass) {
						//spawn rock
						Rock r = new Rock(assets.rock, x*tWH, y*tWH);
						tiles[x][y] = r;
					}
				}
			}
		}
		//Scatter some more around iron city centre
		
		
		return tiles;
	}
	
	private static Tile[][] addTreesToMap(Tile[][] tiles, Assets assets, float tWH) {
		//Scatter some trees randomly on the map
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles.length; y++) {
				float chance = MathUtils.random();
				if(chance < 0.013) {
					if(tiles[x][y] instanceof Grass) {
						//spawn rock
						Tree r = new Tree(assets.tree, x*tWH, y*tWH);
						tiles[x][y] = r;
					}
				}
			}
		}
		//Scatter some more around wood city centre
		
		
		return tiles;
	}
}

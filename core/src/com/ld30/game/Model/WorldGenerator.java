package com.ld30.game.Model;

import com.badlogic.gdx.math.Vector2;
import com.ld30.game.Assets;
import com.ld30.game.Model.Tiles.Grass;
import com.ld30.game.Model.Tiles.Road;
import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.Model.Tiles.Water;

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
	
	public static GeneratedWorld generateMap(Assets assets, float tWH) {
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
		 * Create a river
		 */
		Vector2 riverStart = new Vector2(0f, (float) Math.floor(Math.random()*tiles[0].length));
		Water r1 = new Water(assets.water, tWH*riverStart.x, tWH*riverStart.y);
		tiles[(int)riverStart.x][(int)riverStart.y] = r1;
		Vector2 riverEnd = new Vector2(tiles.length-1, (float) Math.floor(Math.random()*tiles[0].length));
		Water r2 = new Water(assets.water, tWH*riverEnd.x, tWH*riverEnd.y);
		tiles[(int)riverEnd.x][(int)riverEnd.y] = r2;
		
		float currentX = riverStart.x;
		float currentY = riverStart.y;
		
		float currentYminus1 = currentY <= 0 ? 0 : currentY - 1; //TODO quick and dirty
		float currentYplus1 = currentY >= mapHeight - 1 ? mapHeight - 1 : currentY + 1;
		
		if(tiles[(int)currentX][(int)currentYminus1] instanceof Grass) {
			Water t = new Water(assets.water, tWH*currentX, tWH*(currentYminus1));
			tiles[(int)currentX][(int)currentYminus1] = t;
		}
		if(tiles[(int)currentX][(int)currentYplus1] instanceof Grass) {
			Water t = new Water(assets.water, tWH*currentX, tWH*(currentYplus1));
			tiles[(int)currentX][(int)currentYplus1] = t;
		}
		while(currentX != riverEnd.x || currentY != riverEnd.y) {
			if(currentX < riverEnd.x) {
				currentX++;
			} else if(currentX > riverEnd.x) {
				currentX--;
			}
			
			if(currentY < riverEnd.y) {
				currentY++;
			} else if(currentY > riverEnd.y) {
				currentY--;
			}
			
			currentYminus1 = currentY <= 0 ? 0 : currentY - 1; //TODO quick and dirty
			currentYplus1 = currentY >= mapHeight - 1 ? mapHeight - 1 : currentY + 1;
			
			
			Water tile = new Water(assets.water, tWH*currentX, tWH*currentY);
			tiles[(int)currentX][(int)currentY] = tile;
			if(tiles[(int)currentX][(int)currentYminus1] instanceof Grass) {
				Water t = new Water(assets.water, tWH*currentX, tWH*(currentYminus1));
				tiles[(int)currentX][(int)currentYminus1] = t;
			}
			if(tiles[(int)currentX][(int)currentYplus1] instanceof Grass) {
				Water t = new Water(assets.water, tWH*currentX, tWH*(currentYplus1));
				tiles[(int)currentX][(int)currentYplus1] = t;
			}
			
		}
		
		/*
		 * Create city centres
		 */
		Road woodCityCentre, ironCityCentre, foodCityCentre;
		float randomX = (float) Math.floor(Math.random()*(Math.floor(tiles.length/3)))+1;
		float randomY = (float) Math.floor(Math.random()*(tiles[0].length/3))+1;
		foodCityCentre = new Road(assets.city, randomX*tWH, randomY*tWH);
		foodCityCentre.setCenter(GameWorld.Center.FOOD);
		
		tiles[(int)randomX][(int)randomY] = foodCityCentre;
		
		randomX = (float) Math.floor(Math.random()*(Math.floor(tiles.length/3)))+tiles.length/3*2-1;
		randomY = (float) Math.floor(Math.random()*(tiles[0].length/3))+1;
		ironCityCentre = new Road(assets.city, randomX*tWH, randomY*tWH);
		ironCityCentre.setCenter(GameWorld.Center.IRON);
		tiles[(int)randomX][(int)randomY] = ironCityCentre;
		
		randomX = (float) Math.floor(Math.random()*(tiles.length-2))+1;
		randomY = (float) Math.floor(Math.random()*(Math.floor(tiles[0].length/2)))+tiles[0].length/2-1;
		woodCityCentre = new Road(assets.city, randomX*tWH, randomY*tWH);
		woodCityCentre.setCenter(GameWorld.Center.WOOD);
		tiles[(int)randomX][(int)randomY] = woodCityCentre;
		
		final Tile[] centers = new Tile[] {
				woodCityCentre,
				ironCityCentre,
				foodCityCentre
		};
		
		/*
		 * Create roads between city centres
		 */
		//From food centre to iron
		currentX = foodCityCentre.getX()/tWH;
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
			
			Road tile = new Road(assets.road, tWH*currentX, tWH*currentY);
			tiles[(int)currentX][(int)currentY] = tile;
			tile.setCenter(GameWorld.Center.NONE);
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
		}
		//From iron city centre to wood
		currentX = ironCityCentre.getX()/tWH;
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
			
			Road tile = new Road(assets.road, tWH*currentX, tWH*currentY);
			tiles[(int)currentX][(int)currentY] = tile;
			tile.setCenter(GameWorld.Center.NONE);
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
		}
		//From wood city centre to food
		currentX = woodCityCentre.getX()/tWH;
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
			
			Road tile = new Road(assets.road, tWH*currentX, tWH*currentY);
			tiles[(int)currentX][(int)currentY] = tile;
			tile.setCenter(GameWorld.Center.NONE);
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
		}
		
		return new GeneratedWorld(tiles, centers);
	}
	
}

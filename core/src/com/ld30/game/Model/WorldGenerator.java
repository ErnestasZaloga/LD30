package com.ld30.game.Model;

import com.badlogic.gdx.math.Vector2;
import com.ld30.game.Assets;
import com.ld30.game.Model.Tiles.Grass;
import com.ld30.game.Model.Tiles.Road;
import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.Model.Tiles.Water;

public class WorldGenerator {
	
	public static Tile[][] generateMap(Assets assets, float tWH) {
		Tile[][] tiles = new Tile[16][16];
		
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
			
			Water tile = new Water(assets.water, tWH*currentX, tWH*currentY);
			tiles[(int)currentX][(int)currentY] = tile;
		}
		
		/*
		 * Create city centres
		 */
		Road woodCityCentre, ironCityCentre, foodCityCentre;
		float randomX = (float) Math.floor(Math.random()*tiles.length);
		float randomY = (float) Math.floor(Math.random()*tiles[0].length);
		foodCityCentre = new Road(assets.road, randomX*tWH, randomY*tWH);
		foodCityCentre.setCenter(GameWorld.Center.FOOD);
		tiles[(int)randomX][(int)randomY] = foodCityCentre;
		
		randomX = (float) Math.floor(Math.random()*tiles.length);
		randomY = (float) Math.floor(Math.random()*tiles[0].length);
		ironCityCentre = new Road(assets.road, randomX*tWH, randomY*tWH);
		ironCityCentre.setCenter(GameWorld.Center.IRON);
		tiles[(int)randomX][(int)randomY] = ironCityCentre;
		
		randomX = (float) Math.floor(Math.random()*tiles.length);
		randomY = (float) Math.floor(Math.random()*tiles[0].length);
		woodCityCentre = new Road(assets.road, randomX*tWH, randomY*tWH);
		woodCityCentre.setCenter(GameWorld.Center.WOOD);
		tiles[(int)randomX][(int)randomY] = woodCityCentre;
		
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
			
			Road tile = new Road(assets.road, tWH*currentX, tWH*currentY);
			tiles[(int)currentX][(int)currentY] = tile;
			tile.setCenter(GameWorld.Center.NONE);
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
			
			Road tile = new Road(assets.road, tWH*currentX, tWH*currentY);
			tiles[(int)currentX][(int)currentY] = tile;
			tile.setCenter(GameWorld.Center.NONE);
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
			
			Road tile = new Road(assets.road, tWH*currentX, tWH*currentY);
			tiles[(int)currentX][(int)currentY] = tile;
			tile.setCenter(GameWorld.Center.NONE);
		}
		
		return tiles;
	}
	
}

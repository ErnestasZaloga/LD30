package com.ld30.game.Model;

import com.badlogic.gdx.math.Vector2;
import com.ld30.game.Assets;
import com.ld30.game.Model.Tiles.Grass;
import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.Model.Tiles.Water;

public class WorldGenerator {
	
	public static Tile[][] generateMap(Assets assets) {
		Tile[][] tiles = new Tile[16][16];
		
		/*
		 * Fill tile array with grass
		 */
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				Tile t = new Grass(assets.grass, x*32, y*32);
				tiles[x][y] = t;
			}
		}
		
		/*
		 * Make a river
		 */
		Vector2 riverStart = new Vector2(0f, (float) Math.floor(Math.random()*tiles[0].length));
		Tile t = new Water(assets.water, 32*riverStart.x, 32*riverStart.y);
		tiles[(int)riverStart.x][(int)riverStart.y] = t;
		
		Vector2 riverEnd = new Vector2(tiles.length-1, (float) Math.floor(Math.random()*tiles[0].length));
		Tile t1 = new Water(assets.water, 32*riverEnd.x, 32*riverEnd.y);
		tiles[(int)riverEnd.x][(int)riverEnd.y] = t1;
		
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
			
			Tile tile = new Water(assets.water, 32*currentX, 32*currentY);
			tiles[(int)currentX][(int)currentY] = tile;
		}
		
		
		return tiles;
	}
	
}

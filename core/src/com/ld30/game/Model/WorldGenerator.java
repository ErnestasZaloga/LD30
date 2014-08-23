package com.ld30.game.Model;

import com.badlogic.gdx.math.Vector2;
import com.ld30.game.Assets;
import com.ld30.game.Model.Tiles.Grass;
import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.Model.Tiles.Water;

public class WorldGenerator {
	
	public static Tile[][] generateMap(Assets assets) {
		Tile[][] tiles = new Tile[16][16];
		
		//Fill tile array with grass
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				Tile t = new Grass(assets.grass, x*32, y*32);
				tiles[x][y] = t;
			}
		}
		//Make a river
		//int riverStart = (int) Math.floor(Math.random()*tiles[0].length+1);
		Vector2 riverStart = new Vector2(0f, (float) Math.floor(Math.random()*tiles[0].length+1));
		Tile t = new Water(assets.water, 32*riverStart.x, 32*riverStart.y);
		tiles[(int)riverStart.x][(int)riverStart.y] = t;
		
		int riverEnd = (int) Math.floor(Math.random()*tiles[0].length+1);
		Tile t1 = new Water(assets.water, 15*32, 32*riverEnd);
		tiles[15][riverEnd] = t1;
		
		return tiles;
	}
	
}

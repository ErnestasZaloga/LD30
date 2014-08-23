package com.ld30.game.Model;

import com.ld30.game.Assets;
import com.ld30.game.Model.Tiles.Grass;
import com.ld30.game.Model.Tiles.Tile;

public class WorldGenerator {
	
	public static Tile[][] generateMap(Assets assets) {
		Tile[][] tile = new Tile[16][16];
		
		//Fill tile array with grass
		for(int x = 0; x < tile.length; x++) {
			for(int y = 0; y < tile[0].length; y++) {
				Tile t = new Grass(assets.tile, x*32, y*32);
				tile[x][y] = t;
			}
		}
		//Make a river
		//Tile t = new Water(texture, x, y)
		
		return tile;
	}
	
}

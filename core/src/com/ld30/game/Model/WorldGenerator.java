package com.ld30.game.Model;

import com.badlogic.gdx.utils.Array;
import com.ld30.game.Assets;

public class WorldGenerator {
	
	public static Array<Tile> generateMap(Assets assets) {
		Array<Tile> tile = new Array<Tile>();
		
		//Populate tile array
		Tile t = new Grass();
		t.setTexture(assets.tile);
		tile.add(t);
		
		return tile;
	}
	
}

package com.ld30.game.View;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ld30.game.Model.GameWorld;
import com.ld30.game.Model.Tiles.Tile;

public class WorldRenderer {
	
	private final SpriteBatch batch;
	private GameWorld gameWorld;
	
	public WorldRenderer(SpriteBatch batch,
						 GameWorld gameWorld) {
		
		this.batch = batch;
		this.gameWorld = gameWorld;
	}
	
	public void render() {
		batch.begin();
		Tile[][] tiles = gameWorld.getMap().getTiles();
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				batch.draw(tiles[x][y].getTexture(), tiles[x][y].getX(), tiles[x][y].getY());
			}
		}
		batch.end();
	}

}

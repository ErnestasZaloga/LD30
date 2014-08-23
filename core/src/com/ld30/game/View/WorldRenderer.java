package com.ld30.game.View;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.ld30.game.Model.GameWorld;
import com.ld30.game.Model.Tile;

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
		Array<Tile> tiles = gameWorld.getTiles();
		for (Tile tile : tiles) {
			batch.draw(tile.getTexture(), tile.getX(), tile.getY());
		}
		batch.end();
	}

}

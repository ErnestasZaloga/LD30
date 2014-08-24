package com.ld30.game.View;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.ld30.game.Model.City;
import com.ld30.game.Model.GameWorld;
import com.ld30.game.Model.MoveableEntity;
import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.Model.moveable.Humanoid;

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
		batch.setColor(1f, 1f, 1f, 1f);
		Tile[][] tiles = gameWorld.getMap().getTiles();
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				batch.draw(
						tiles[x][y].getTexture(), 
						tiles[x][y].getX(), 
						tiles[x][y].getY(),
						gameWorld.getMap().getTileWidth(),
						gameWorld.getMap().getTileHeight()
						);
			}
		}
		
		final Array<MoveableEntity> entities = gameWorld.getEntities();
		for (int x = 0; x < entities.size; x += 1) {
			final MoveableEntity entity = entities.get(x);
			batch.draw(entity.getTexture(),
					entity.getX(),
					entity.getY(),
					//entity.getWidth(),
					//entity.getHeight()
					gameWorld.getMap().getTileWidth(),
					gameWorld.getMap().getTileHeight()
					);
		}
		
		batch.setColor(1f, 1f, 1f, 0.5f);
		final Array<City> cities = gameWorld.getCities();
		for (int x = 0; x < cities.size; x += 1) {
			final City city = cities.get(x);
			batch.draw(city.getTexture(), city.getX(), city.getY());
		}
		
		batch.end();
		gameWorld.getUI().updateAndRender(batch);
	}
	
}

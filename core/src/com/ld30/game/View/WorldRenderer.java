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
		Tile[][] tiles = gameWorld.getMap().getTiles();
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				batch.draw(tiles[x][y].getTexture(), tiles[x][y].getX(), tiles[x][y].getY());
			}
		}
		
		
		final Array<MoveableEntity> entities = gameWorld.getEntities();
		for (int x = 0; x < entities.size; x += 1) {
			final MoveableEntity entity = entities.get(x);
			batch.draw(entity.getTexture(), entity.getX(), entity.getY());
			
			if (entity instanceof Humanoid) {
				Humanoid humanoid = (Humanoid) entity;
				
				for (int i = 0; i < humanoid.getWalkPath().size; i += 2) {
					batch.draw(entity.getTexture(), 
							gameWorld.getMap().getTileWidth() * humanoid.getWalkPath().get(i),
							gameWorld.getMap().getTileHeight() * humanoid.getWalkPath().get(i + 1));
				}
			}
		}
		
		final Array<City> cities = gameWorld.getCities();
		for (int x = 0; x < cities.size; x += 1) {
			final City city = cities.get(x);
			batch.draw(city.getTexture(), city.getX(), city.getY());
		}
		
		batch.end();
	}
	
}

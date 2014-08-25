package com.ld30.game.View;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.ld30.game.Model.City;
import com.ld30.game.Model.Decal;
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
				final float darkenPercent = tiles[x][y].getDarkenPercent();
				batch.setColor(1f - darkenPercent, 1f - darkenPercent, 1f - darkenPercent, 1f);
				batch.draw(
						tiles[x][y].getTexture(), 
						tiles[x][y].getX(), 
						tiles[x][y].getY(),
						gameWorld.getMap().getTileWidth(),
						gameWorld.getMap().getTileHeight()
						);
			}
		}
		
		final Array<Decal> groundDecals = gameWorld.getGroundDecals();
		for (int x = 0; x < groundDecals.size; x += 1) {
			final Decal decal = groundDecals.get(x);
			batch.setColor(1f, 1f, 1f, 1f * (1f - decal.getPercent()));
			batch.draw(decal.getTexture(), decal.getX(), decal.getY(), decal.getWidth(), decal.getHeight());
		}
		
		batch.setColor(1f, 1f, 1f, 1f);
		final Array<MoveableEntity> entities = gameWorld.getEntities();
		for (int x = 0; x < entities.size; x += 1) {
			final MoveableEntity entity = entities.get(x);
			float displacement = 0f;
			if (entity instanceof Humanoid) {
				final Humanoid humanoid = (Humanoid) entity;
				if (humanoid.isGoingUp()) {
					displacement = humanoid.getHeight() * 0.1f;
				}
				else {
					displacement = 0f;
				}
			}
			
			batch.draw(entity.getTexture(),
					entity.getX(),
					entity.getY() + displacement,
					entity.getWidth(),
					entity.getHeight()
					);
		}
		
		batch.setColor(1f, 1f, 1f, 1f);
		
		final Array<City> cities = gameWorld.getCities();
		for (int x = 0; x < cities.size; x += 1) {
			final City city = cities.get(x);
			batch.draw(city.getTexture(), city.getX(), city.getY(), city.getWidth(), city.getHeight());
		}
		
		final Array<Decal> decals = gameWorld.getDecals();
		for (int x = 0; x < decals.size; x += 1) {
			final Decal decal = decals.get(x);
			batch.setColor(1f, 1f, 1f, 1f * (1f - decal.getPercent()));
			batch.draw(decal.getTexture(), decal.getX(), decal.getY(), decal.getWidth(), decal.getHeight());
		}
		
		batch.end();
		gameWorld.getUI().updateAndRender(batch);
	}
	
}

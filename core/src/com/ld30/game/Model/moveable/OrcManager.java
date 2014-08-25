package com.ld30.game.Model.moveable;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.ld30.game.Model.Blockade;
import com.ld30.game.Model.City;
import com.ld30.game.Model.GameWorld;
import com.ld30.game.Model.Map;
import com.ld30.game.Model.Tiles.Rock;
import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.Model.Tiles.Tree;
import com.ld30.game.Model.Tiles.Water;
import com.ld30.game.utils.AStar;
import com.ld30.game.utils.Log;

public class OrcManager {
	
	private class PositionResult {
		public int x;
		public int y;
		public IntArray path;
	}

	private float stateTime;
	private final GameWorld gameWorld;
	private final Array<Blockade> inactiveBlockades = new Array<Blockade> ();
	private final IntArray tmpPath = new IntArray();

	private final AStar.Validator blockadeOrcMovementValidator = new AStar.Validator() {
		@Override
		public boolean isValid(int x, int y) {
			final Tile tile = gameWorld.getMap().getTile(x, y);
			boolean noCityCollision = true;
			
			final float tileX = tile.getX();
			final float tileY = tile.getY();
			final Array<City> cities = gameWorld.getCities();
			for (int i = 0; i < cities.size; i += 1) {
				final City city = cities.get(i);
				
				if (tileX >= city.getX() && tileX <= city.getX() + city.getWidth() &&
					tileY >= city.getY() && tileY <= city.getY() + city.getHeight()) {
					
					noCityCollision = false;
					break;
				}
			}
			
			return !(tile instanceof Water) && !(tile instanceof Rock) && !(tile instanceof Tree) && noCityCollision;
		}
	};
	private final AStar.Validator orcMovementValidator = new AStar.Validator() {
		@Override
		public boolean isValid(int x, int y) {
			final Tile tile = gameWorld.getMap().getTile(x, y);
			return !(tile instanceof Water) && !(tile instanceof Rock) && !(tile instanceof Tree);
		}
	};
	
	public OrcManager (final GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}
	
	private final PositionResult tmpPositionResult = new PositionResult ();
	private PositionResult findPosition (final int targetTileX, 
										 final int targetTileY,
										 final float targetX,
										 final float targetY,
										 final boolean reverse,
										 final AStar.Validator validator) {
		
		final AStar aStar = gameWorld.getAStar();
		final Map map = gameWorld.getMap();
		final float centerX = (map.getTileWidth() * map.getWidth()) / 2f;
		final float centerY = (map.getTileHeight() * map.getHeight()) / 2f;
		
		tmpPositionResult.path = tmpPath;
		int posX = 0;
		int posY = 0;
		
		boolean bottom = targetY <= centerY;
		boolean left = targetX <= centerX;
		
		if (reverse && MathUtils.randomBoolean()) {
			bottom = !bottom;
			left = !left;
		}
		
		if (bottom && left) {
			final int x = 0;
			final int y = 0;
			final int yHigh = map.getHeight() / 2;
			final int xHigh = map.getWidth() / 2;

			while (true) {
				if (MathUtils.randomBoolean(0.5f)) {
					posX = x;
					posY = MathUtils.random(y, yHigh - 1);
					
					tmpPath.addAll(aStar.getPath(posX, posY, targetTileX, targetTileY, validator));
					if (tmpPath.size != 0) {
						break;
					}
				}
				else {
					posX = MathUtils.random(x, xHigh - 1);
					posY = y;
					
					tmpPath.addAll(aStar.getPath(posX, posY, targetTileX, targetTileY, validator));
					if (tmpPath.size != 0) {
						break;
					}
				}
			}
		}
		else if (left && !bottom) {
			final int x = 0;
			final int y = map.getHeight() / 2;
			final int xHigh = map.getWidth() / 2;
			final int yHigh = map.getHeight();
			
			while (true) {
				if (MathUtils.randomBoolean(0.5f)) {
					posX = x;
					posY = MathUtils.random(y, yHigh - 1);
					
					tmpPath.addAll(aStar.getPath(posX, posY, targetTileX, targetTileY, validator));
					if (tmpPath.size != 0) {
						break;
					}
				}
				else {
					posX = MathUtils.random(x, xHigh - 1);
					posY = yHigh - 1;
					
					tmpPath.addAll(aStar.getPath(posX, posY, targetTileX, targetTileY, validator));
					if (tmpPath.size != 0) {
						break;
					}
				}
			}
		}
		else if (!left && bottom) {
			final int x = map.getWidth() / 2;
			final int y = 0;
			final int xHigh = map.getWidth();
			final int yHigh = map.getHeight() / 2;
			
			while (true) {
				if (MathUtils.randomBoolean(0.5f)) {
					posX = xHigh - 1;
					posY = MathUtils.random(y, yHigh - 1);
					
					tmpPath.addAll(aStar.getPath(posX, posY, targetTileX, targetTileY, validator));
					if (tmpPath.size != 0) {
						break;
					}
				}
				else {
					posX = MathUtils.random(x, xHigh - 1);
					posY = y;
					
					tmpPath.addAll(aStar.getPath(posX, posY, targetTileX, targetTileY, validator));
					if (tmpPath.size != 0) {
						break;
					}
				}
			}
		}
		else if (!left && !bottom) {
			final int x = map.getWidth() / 2;
			final int y = map.getHeight() / 2;
			final int xHigh = map.getWidth();
			final int yHigh = map.getHeight();
			
			while (true) {
				if (MathUtils.randomBoolean(0.5f)) {
					posX = xHigh - 1;
					posY = MathUtils.random(y, yHigh - 1);
					
					tmpPath.addAll(aStar.getPath(posX, posY, targetTileX, targetTileY, validator));
					if (tmpPath.size != 0) {
						break;
					}
				}
				else {
					posX = MathUtils.random(x, xHigh - 1);
					posY = yHigh - 1;
					
					tmpPath.addAll(aStar.getPath(posX, posY, targetTileX, targetTileY, validator));
					if (tmpPath.size != 0) {
						break;
					}
				}
			}
		}
		
		tmpPositionResult.x = posX;
		tmpPositionResult.y = posY;
		
		return tmpPositionResult;
	}
	
	public void update (final float delta) {
		stateTime += delta;
		
		if (stateTime > 2f) {
			stateTime = 0f;
			
			inactiveBlockades.clear();
			
			final Blockade[] blockades = gameWorld.getBlockades();
			for (int i = 0; i < blockades.length; i += 1) {
				if (!blockades[i].isActive() && blockades[i].getOwner() == null) {
					inactiveBlockades.add(blockades[i]);
				}
			}
			
			boolean isBlockade = MathUtils.randomBoolean(inactiveBlockades.size == 0 ? 0f : 0.5f);
			
			final Map map = gameWorld.getMap();
			tmpPath.clear();
			
			if (isBlockade) {
				final Blockade blockade = inactiveBlockades.random();
				
				final float blockadeX = blockade.getTile().getX();
				final float blockadeY = blockade.getTile().getY();
				
				final int blockadeTileX = (int)(map.getWidth() * (blockadeX / (map.getWidth() * map.getTileWidth())));
				final int blockadeTileY = (int)(map.getHeight() * (blockadeY / (map.getHeight() * map.getTileHeight())));

				PositionResult pos = findPosition(blockadeTileX, blockadeTileY, blockadeX, blockadeY, false, blockadeOrcMovementValidator);
				if (pos.path.size != 0) {
					final BlockadeOrc orc = new BlockadeOrc();
					orc.setBlockade(blockade);
					orc.setDestination(blockadeTileX, blockadeTileY);
					orc.getWalkPath().addAll(pos.path);
					orc.setLastPosition(pos.x, pos.y);
					
					orc.setX(pos.x * map.getTileWidth());
					orc.setY(pos.y * map.getTileHeight());
					
					orc.setState(Humanoid.State.WALKING);

					orc.setMovementValidator(blockadeOrcMovementValidator);
					orc.setPixelsPerSecond(map.getPixelsPerSecond());
					orc.setTexture(gameWorld.getAssets().blockadeOrc);
					orc.setWidth(map.getTileWidth());
					orc.setHeight(map.getTileHeight());
					
					gameWorld.getEntities().add(orc);
					
					blockade.setOwner(orc);
				}
				else {
				}
			}
			else {
				final City city = gameWorld.getCities().random();
				Tile cityTile;
				float cityX;
				float cityY;
				int cityTileX;
				int cityTileY;
				PositionResult pos;
				
				while(true) {
					cityTile = city.getCitySurroundingTiles().random();
					
					cityX = cityTile.getX();
					cityY = cityTile.getY();
					
					cityTileX = (int)(map.getWidth() * (cityX / (map.getWidth() * map.getTileWidth())));
					cityTileY = (int)(map.getHeight() * (cityY / (map.getHeight() * map.getTileHeight())));
				
					pos = findPosition(cityTileX, cityTileY, cityX, cityY, true, orcMovementValidator);
					
					if (pos.path.size != 0) {
						break;
					}
					
					pos.path.clear();
				}
				
				if (pos.path.size != 0) {
					final BruteOrc orc = new BruteOrc();
					orc.setTarget(city);
					orc.setDestination(cityTileX, cityTileY);
					//Log.trace(this, "dstx", cityTileX, cityTileY);
					orc.getWalkPath().addAll(pos.path);
					orc.setLastPosition(pos.x, pos.y);
					
					orc.setX(pos.x * map.getTileWidth());
					orc.setY(pos.y * map.getTileHeight());
					
					orc.setState(Humanoid.State.WALKING);

					orc.setMovementValidator(blockadeOrcMovementValidator);
					orc.setPixelsPerSecond(map.getPixelsPerSecond());
					orc.setTexture(gameWorld.getAssets().bruteOrc);
					orc.setWidth(map.getTileWidth());
					orc.setHeight(map.getTileHeight());
					
					gameWorld.getEntities().add(orc);
				}
			}
		}
	}
	
}

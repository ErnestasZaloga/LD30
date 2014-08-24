package com.ld30.game.Model.moveable;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.ld30.game.Model.Blockade;
import com.ld30.game.Model.GameWorld;
import com.ld30.game.Model.Map;
import com.ld30.game.Model.Tiles.Rock;
import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.Model.Tiles.Tree;
import com.ld30.game.Model.Tiles.Water;
import com.ld30.game.utils.AStar;

public class OrcManager {
	
	private class PositionResult {
		public int x;
		public int y;
		public IntArray path;
	}

	private float stateTime;
	private final GameWorld gameWorld;
	private final Array<Blockade> pendingBlockades = new Array<Blockade> ();
	private final Array<Blockade> inactiveBlockades = new Array<Blockade> ();
	private final IntArray tmpPath = new IntArray();
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
	
	public void removePendingBlockade (final Blockade blockade) {
		pendingBlockades.removeValue(blockade, true);
	}
	
	private final PositionResult tmpPositionResult = new PositionResult ();
	private PositionResult findPosition (final int targetTileX, 
										 final int targetTileY,
										 final float targetX,
										 final float targetY) {
		
		final AStar aStar = gameWorld.getAStar();
		final Map map = gameWorld.getMap();
		final float centerX = (map.getTileWidth() * map.getWidth()) / 2f;
		final float centerY = (map.getTileHeight() * map.getHeight()) / 2f;
		
		tmpPositionResult.path = tmpPath;
		int posX = 0;
		int posY = 0;
		
		if (targetX < centerX && targetY < centerY) {
			final int x = 0;
			final int y = 0;
			final int yHigh = map.getHeight() / 2;
			final int xHigh = map.getWidth() / 2;

			while (true) {
				if (MathUtils.randomBoolean(0.5f)) {
					posX = x;
					posY = MathUtils.random(y, yHigh);
					
					tmpPath.addAll(aStar.getPath(posX, posY, targetTileX, targetTileY, orcMovementValidator));
					if (tmpPath.size != 0) {
						break;
					}
				}
				else {
					posX = MathUtils.random(x, xHigh);
					posY = y;
					
					tmpPath.addAll(aStar.getPath(posX, posY, targetTileX, targetTileY, orcMovementValidator));
					if (tmpPath.size != 0) {
						break;
					}
				}
			}
		}
		else if (targetX < centerX && targetY > centerY) {
			final int x = 0;
			final int y = map.getHeight() / 2;
			final int xHigh = map.getWidth() / 2;
			final int yHigh = map.getHeight();
			
			while (true) {
				if (MathUtils.randomBoolean(0.5f)) {
					posX = x;
					posY = MathUtils.random(y, yHigh);
					
					tmpPath.addAll(aStar.getPath(posX, posY, targetTileX, targetTileY, orcMovementValidator));
					if (tmpPath.size != 0) {
						break;
					}
				}
				else {
					posX = MathUtils.random(x, xHigh);
					posY = y;
					
					tmpPath.addAll(aStar.getPath(posX, posY, targetTileX, targetTileY, orcMovementValidator));
					if (tmpPath.size != 0) {
						break;
					}
				}
			}
		}
		else if (targetX > centerX && targetY < centerY) {
			final int x = map.getWidth() / 2;
			final int y = 0;
			final int xHigh = map.getWidth();
			final int yHigh = map.getHeight() / 2;
			
			while (true) {
				if (MathUtils.randomBoolean(0.5f)) {
					posX = xHigh - 1;
					posY = MathUtils.random(y, yHigh);
					
					tmpPath.addAll(aStar.getPath(posX, posY, targetTileX, targetTileY, orcMovementValidator));
					if (tmpPath.size != 0) {
						break;
					}
				}
				else {
					posX = MathUtils.random(x, xHigh);
					posY = y;
					
					tmpPath.addAll(aStar.getPath(posX, posY, targetTileX, targetTileY, orcMovementValidator));
					if (tmpPath.size != 0) {
						break;
					}
				}
			}
		}
		else if (targetX > centerX && targetY > centerY) {
			final int x = map.getWidth() / 2;
			final int y = map.getHeight() / 2;
			final int xHigh = map.getWidth();
			final int yHigh = map.getHeight();
			
			while (true) {
				if (MathUtils.randomBoolean(0.5f)) {
					posX = xHigh - 1;
					posY = MathUtils.random(y, yHigh);
					
					tmpPath.addAll(aStar.getPath(posX, posY, targetTileX, targetTileY, orcMovementValidator));
					if (tmpPath.size != 0) {
						break;
					}
				}
				else {
					posX = MathUtils.random(x, xHigh);
					posY = yHigh - 1;
					
					tmpPath.addAll(aStar.getPath(posX, posY, targetTileX, targetTileY, orcMovementValidator));
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
		
		if (stateTime > 0.1f) {
			stateTime = 0f;
			
			inactiveBlockades.clear();
			
			final Blockade[] blockades = gameWorld.getBlockades();
			for (int i = 0; i < blockades.length; i += 1) {
				if (!blockades[i].isActive() && !pendingBlockades.contains(blockades[i], true)) {
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

				PositionResult pos = findPosition(blockadeTileX, blockadeTileY, blockadeX, blockadeY);
				
				if (pos.path.size != 0) {
					final BlockadeOrc orc = new BlockadeOrc();
					orc.setBlockade(blockade);
					orc.setDestination(blockadeTileX, blockadeTileY);
					orc.getWalkPath().addAll(pos.path);
					orc.setLastPosition(pos.x, pos.y);
					
					orc.setX(pos.x * map.getTileWidth());
					orc.setY(pos.y * map.getTileHeight());
					
					orc.setState(Humanoid.State.WALKING);

					orc.setMovementValidator(orcMovementValidator);
					orc.setPixelsPerSecond(64);
					orc.setTexture(gameWorld.getAssets().moveable);
	
					gameWorld.getEntities().add(orc);
					
					pendingBlockades.add(blockade);
				}
			}
		}
	}
	
}

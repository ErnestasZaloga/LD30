package com.ld30.game.Model.moveable;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ld30.game.Model.Blockade;
import com.ld30.game.Model.GameWorld;
import com.ld30.game.Model.Map;
import com.ld30.game.Model.MoveableEntity;
import com.ld30.game.Model.Tiles.Road;
import com.ld30.game.Model.Tiles.ShallowWater;
import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.utils.AStar;

public class MovableManager {

	private Map map;
	private GameWorld gameWorld;
	private AStar.Validator roadAStarValidation = new AStar.Validator() {
		@Override
		public boolean isValid(int x, int y) {
			return map.getTiles()[x][y] instanceof Road || map.getTiles()[x][y] instanceof ShallowWater;
		}
	};
	
	public MovableManager (final GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		this.map = gameWorld.getMap();
	}
	
	public void setupRoadMovement (final Humanoid humanoid,
								   final int currentPositionX,
								   final int currentPositionY,
								   final int targetX,
								   final int targetY) {
		
		humanoid.getWalkPath().clear();
		humanoid.getWalkPath().addAll(gameWorld.getAStar().getPath(currentPositionX, currentPositionY, targetX, targetY, roadAStarValidation));
		humanoid.setDestination(targetX, targetY);
	}
	
	Vector2 tmpVector = new Vector2();
	public void update (final float delta) {
		final Array<MoveableEntity> movables = gameWorld.getEntities();
		final AStar astar = gameWorld.getAStar();
		final Map map = gameWorld.getMap();
		final Blockade[] blockades = gameWorld.getBlockades();
		
		for (int i = 0; i < movables.size; i += 1) {
			final MoveableEntity movable = movables.get(i);
			
			if (movable instanceof Humanoid) {
				Humanoid humanoid = (Humanoid) movable;

				if (humanoid.getState() == Humanoid.State.IDLE) {
					continue;
				}
				
				final float humanoidX = humanoid.getX() + humanoid.getWidth() / 2f;
				final float humanoidY = humanoid.getY() + humanoid.getHeight() / 2f;
				
				final int currentPositionX = (int)(map.getWidth() * (humanoidX / (map.getWidth() * map.getTileWidth())));
				final int currentPositionY = (int)(map.getHeight() * (humanoidY / (map.getHeight() * map.getTileHeight())));
				
				if (humanoid.getWalkPath().size != 0 && 
					!((currentPositionX == humanoid.getDestinationX()) && 
					(currentPositionY == humanoid.getDestinationY()))) {
					
					int nextX = humanoid.getWalkPath().get(humanoid.getWalkPath().size - 2);
					int nextY = humanoid.getWalkPath().get(humanoid.getWalkPath().size - 1);

					boolean proceedMovement = true;
					if (currentPositionX != humanoid.getLastPositionX() ||
						currentPositionY != humanoid.getLastPositonY()) {
							
						if ((humanoid instanceof Worker || humanoid instanceof Troop)) {
							for (int ii = 0; ii < blockades.length; ii += 1) {
								if (!blockades[i].isActive()) {
									continue;
								}
								
								int tileX = (int)(map.getWidth() * (blockades[i].getTile().getX() / (map.getWidth() * map.getTileWidth())));
								int tileY = (int)(map.getHeight() * (blockades[i].getTile().getY() / (map.getHeight() * map.getTileHeight())));
								
								if (tileX == nextX && tileY == nextY) {
									if (humanoid instanceof Worker) {
										// TODO: reduce resources
										// TODO: make worker go back
										Worker worker = (Worker) humanoid;
										worker.setResourcesCarried(0);
										
										proceedMovement = false;
									}
									else {
										blockades[i].setActive(false);
										if (MathUtils.randomBoolean(0.5f)) {
											movables.removeIndex(i);
											--i;
											proceedMovement = false;
										}
									}
								}
							}
						}
						
						humanoid.setLastPosition(currentPositionX, currentPositionY);
					}
					
					if (!proceedMovement) {
						continue;
					}
					
					final Tile nextTile = map.getTile(nextX, nextY);
					
					tmpVector.x = (nextTile.getX() + nextTile.getWidth() / 2f) - (humanoidX);
					tmpVector.y = (nextTile.getY() + nextTile.getHeight() / 2f) - (humanoidY);

					final float length = tmpVector.len();
					
					final float angle = tmpVector.angle();
					tmpVector.x = humanoid.getPixelsPerSecond() * (delta / 1f);
					tmpVector.y = 0;
					
					tmpVector.setAngle(angle);
					if (tmpVector.len() > length) {
						tmpVector.limit(length);
					}
					
					humanoid.setX(humanoid.getX() + tmpVector.x);
					humanoid.setY(humanoid.getY() + tmpVector.y);
				}
				else {
					// TODO: flush to city
				}
			}
		}
	} 
	
}

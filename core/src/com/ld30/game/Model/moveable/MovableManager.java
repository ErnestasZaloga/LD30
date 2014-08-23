package com.ld30.game.Model.moveable;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ld30.game.Model.GameWorld;
import com.ld30.game.Model.Map;
import com.ld30.game.Model.MoveableEntity;
import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.utils.AStar;
import com.ld30.game.utils.Log;

public class MovableManager {

	private GameWorld gameWorld;
	private AStar.Validator workerAStarValidation = new AStar.Validator() {
		@Override
		public boolean isValid(int x, int y) {
			return true;
		}
	};
	
	public MovableManager (final GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}
	
	Vector2 tmpVector = new Vector2();
	public void update (final float delta) {
		final Array<MoveableEntity> movables = gameWorld.getEntities();
		final AStar astar = gameWorld.getAStar();
		final Map map = gameWorld.getMap();
		
		for (int i = 0; i < movables.size; i += 1) {
			final MoveableEntity movable = movables.get(i);
			
			if (movable instanceof Humanoid) {
				Humanoid humanoid = (Humanoid) movable;
				
				if (humanoid.getState() == Humanoid.State.IDLE) {
					continue;
				}
				
				final int currentPositionX = (int)(map.getWidth() * (humanoid.getX() / (map.getWidth() * map.getTileWidth())));
				final int currentPositionY = (int)(map.getHeight() * (humanoid.getY() / (map.getHeight() * map.getTileHeight())));
				
				if (currentPositionX != humanoid.getLastPositionX() ||
					currentPositionY != humanoid.getLastPositonY()) {
					
					Log.trace(this, "New path from", currentPositionX, currentPositionY);
					humanoid.getWalkPath().clear();
					humanoid.getWalkPath().addAll(
							astar.getPath(
									currentPositionX, 
									currentPositionY, 
									humanoid.getDestinationX(), 
									humanoid.getDestinationY(), 
									workerAStarValidation));
				
					humanoid.setLastPosition(currentPositionX, currentPositionY);
				}
				
				if (humanoid.getWalkPath().size > 0) {
					final int nextX = humanoid.getWalkPath().get(0);
					final int nextY = humanoid.getWalkPath().get(1);
					
					final Tile currentTile = map.getTile(currentPositionX, currentPositionY);
					final Tile nextTile = map.getTile(nextX, nextY);
					
					tmpVector.x = nextTile.getX() - currentTile.getX();
					tmpVector.y = nextTile.getY() - currentTile.getY();
					
					final float angle = tmpVector.angle();
					tmpVector.x = humanoid.getPixelsPerSecond() * (delta / 1f);
					tmpVector.y = 0;
					
					tmpVector.setAngle(angle);
					humanoid.setX(humanoid.getX() + tmpVector.x);
					humanoid.setY(humanoid.getY() + tmpVector.y);
				}
				else {
					humanoid.setDestination(MathUtils.random(0, map.getWidth() - 1), MathUtils.random(0, map.getHeight() - 1));
					humanoid.getWalkPath().addAll(
							astar.getPath(
									currentPositionX, 
									currentPositionY, 
									humanoid.getDestinationX(), 
									humanoid.getDestinationY(), 
									workerAStarValidation));
				}
			}
		}
	} 
	
}

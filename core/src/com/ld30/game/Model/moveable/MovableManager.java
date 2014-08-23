package com.ld30.game.Model.moveable;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ld30.game.Model.GameWorld;
import com.ld30.game.Model.Map;
import com.ld30.game.Model.MoveableEntity;
import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.Model.Tiles.Water;
import com.ld30.game.utils.AStar;

public class MovableManager {

	private Map map;
	private GameWorld gameWorld;
	private AStar.Validator workerAStarValidation = new AStar.Validator() {
		@Override
		public boolean isValid(int x, int y) {
			return !(map.getTiles()[x][y] instanceof Water);
		}
	};
	
	public MovableManager (final GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		this.map = gameWorld.getMap();
	}
	
	public void move (final Worker worker) {
		worker.setState(Humanoid.State.WALKING);
		worker.getWalkPath().clear();
		worker.getWalkPath().addAll(
				gameWorld.getAStar().getPath(
						0, 
						0, 
						worker.getDestinationX(), 
						worker.getDestinationY(), 
						workerAStarValidation));
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
				
				final float humanoidX = humanoid.getX() + humanoid.getWidth() / 2f;
				final float humanoidY = humanoid.getY() + humanoid.getHeight() / 2f;
				
				final int currentPositionX = (int)(map.getWidth() * (humanoidX / (map.getWidth() * map.getTileWidth())));
				final int currentPositionY = (int)(map.getHeight() * (humanoidY / (map.getHeight() * map.getTileHeight())));
				
				if (currentPositionX != humanoid.getLastPositionX() ||
					currentPositionY != humanoid.getLastPositonY()) {
					
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
				
				if (humanoid.getWalkPath().size != 0) {
					int nextX = 0;
					int nextY = 0;

					if (humanoid.getWalkPath().size == 0) {
						nextX = humanoid.getDestinationX();
						nextY = humanoid.getDestinationY();
					}
					else {
						nextX = humanoid.getWalkPath().get(0);
						nextY = humanoid.getWalkPath().get(1);
					}
					
					final Tile nextTile = map.getTile(nextX, nextY);
					
					tmpVector.x = (nextTile.getX() + nextTile.getWidth() / 2f) - (humanoidX);
					tmpVector.y = (nextTile.getY() + nextTile.getHeight() / 2f) - (humanoidY);
					
					final float angle = tmpVector.angle();
					tmpVector.x = humanoid.getPixelsPerSecond() * (delta / 1f);
					tmpVector.y = 0;
					
					tmpVector.setAngle(angle);
					humanoid.setX(humanoid.getX() + tmpVector.x);
					humanoid.setY(humanoid.getY() + tmpVector.y);
				}
				else {
					humanoid.setDestination(MathUtils.random(0, map.getWidth() - 1), MathUtils.random(0, map.getHeight() - 1));
					humanoid.getWalkPath().clear();
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

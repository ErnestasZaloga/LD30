package com.ld30.game.Model.moveable;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ld30.game.Model.GameWorld;
import com.ld30.game.Model.Map;
import com.ld30.game.Model.MoveableEntity;
import com.ld30.game.Model.Tiles.Road;
import com.ld30.game.Model.Tiles.ShallowWater;
import com.ld30.game.Model.Tiles.Tile;
import com.ld30.game.utils.AStar;
import com.ld30.game.utils.Log;

public class MovableManager {

	private Map map;
	private GameWorld gameWorld;
	private AStar.Validator workerAStarValidation = new AStar.Validator() {
		@Override
		public boolean isValid(int x, int y) {
			return map.getTiles()[x][y] instanceof Road || map.getTiles()[x][y] instanceof ShallowWater;
		}
	};
	
	public MovableManager (final GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		this.map = gameWorld.getMap();
	}
	
	public void move (final Worker worker) {
		worker.setState(Humanoid.State.WALKING);
		/*worker.getWalkPath().clear();
		worker.getWalkPath().addAll(
				gameWorld.getAStar().getPath(
						0, 
						0, 
						worker.getDestinationX(), 
						worker.getDestinationY(), 
						workerAStarValidation));*/
	}
	
	Vector2 tmpVector = new Vector2();
	public void update (final float delta) {
		final Array<MoveableEntity> movables = gameWorld.getEntities();
		final AStar astar = gameWorld.getAStar();
		final Map map = gameWorld.getMap();
		final Tile[] cityCenters = gameWorld.getCityCenters();
		
		for (int i = 0; i < movables.size; i += 1) {
			final MoveableEntity movable = movables.get(i);
			
			if (movable instanceof Humanoid) {
				Humanoid humanoid = (Humanoid) movable;

				if (humanoid.getState() == Humanoid.State.IDLE) {
					if (humanoid instanceof Worker) {
						Worker worker = (Worker) humanoid;
						final int startCenter = MathUtils.random(0, cityCenters.length - 1);

						humanoid.setX(cityCenters[startCenter].getX());
						humanoid.setY(cityCenters[startCenter].getY());

						int x = 0;
						int y = 0;
						
						worker.setLastCity(startCenter);
						
						if (worker.getLastCity() == 0) {
							worker.setLastCity(1);
							x = (int)(map.getWidth() * (cityCenters[1].getX() / (map.getWidth() * map.getTileWidth())));
							y = (int)(map.getHeight() * (cityCenters[1].getY() / (map.getHeight() * map.getTileHeight())));
						}
						else if (worker.getLastCity() == 1) {
							worker.setLastCity(2);
							x = (int)(map.getWidth() * (cityCenters[2].getX() / (map.getWidth() * map.getTileWidth())));
							y = (int)(map.getHeight() * (cityCenters[2].getY() / (map.getHeight() * map.getTileHeight())));
						}
						else if (worker.getLastCity() == 2) {
							worker.setLastCity(0);
							x = (int)(map.getWidth() * (cityCenters[0].getX() / (map.getWidth() * map.getTileWidth())));
							y = (int)(map.getHeight() * (cityCenters[0].getY() / (map.getHeight() * map.getTileHeight())));
						}
						//Log.trace(this, "Destination", x, y);
						humanoid.setDestination(x, y);
						//humanoid.setDestination(MathUtils.random(0, map.getWidth() - 1), MathUtils.random(0, map.getHeight() - 1));
					}
					
					humanoid.setState(Humanoid.State.WALKING);
					//continue;
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
				
				if (humanoid.getWalkPath().size != 0 && 
					!((currentPositionX == humanoid.getDestinationX()) && 
					(currentPositionY == humanoid.getDestinationY()))) {
					
					int nextX = humanoid.getWalkPath().get(humanoid.getWalkPath().size - 2);
					int nextY = humanoid.getWalkPath().get(humanoid.getWalkPath().size - 1);

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
					
					int x = 0;
					int y = 0;

					if (humanoid instanceof Worker) {
						final Worker worker = (Worker) humanoid;
						
						if (worker.getLastCity() == 0) {
							worker.setLastCity(1);
							x = (int)(map.getWidth() * (cityCenters[1].getX() / (map.getWidth() * map.getTileWidth())));
							y = (int)(map.getHeight() * (cityCenters[1].getY() / (map.getHeight() * map.getTileHeight())));
						}
						else if (worker.getLastCity() == 1) {
							worker.setLastCity(2);
							x = (int)(map.getWidth() * (cityCenters[2].getX() / (map.getWidth() * map.getTileWidth())));
							y = (int)(map.getHeight() * (cityCenters[2].getY() / (map.getHeight() * map.getTileHeight())));
						}
						else if (worker.getLastCity() == 2) {
							worker.setLastCity(0);
							x = (int)(map.getWidth() * (cityCenters[0].getX() / (map.getWidth() * map.getTileWidth())));
							y = (int)(map.getHeight() * (cityCenters[0].getY() / (map.getHeight() * map.getTileHeight())));
						}
					}
					
					//Log.trace(this, "No options left generating new path", humanoid.getWalkPath().size);
					//Log.trace(this, "New dst", x, y);
					//Log.trace(this, "Last dst", humanoid.getDestinationX(), humanoid.getDestinationY());
					humanoid.setDestination(x, y);
					//humanoid.setDestination(MathUtils.random(0, map.getWidth() - 1), MathUtils.random(0, map.getHeight() - 1));
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

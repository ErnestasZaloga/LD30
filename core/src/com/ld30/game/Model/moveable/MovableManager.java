package com.ld30.game.Model.moveable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ld30.game.Model.Blockade;
import com.ld30.game.Model.City;
import com.ld30.game.Model.Decal;
import com.ld30.game.Model.GameWorld;
import com.ld30.game.Model.GameWorld.ResourceType;
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
		humanoid.setMovementValidator(roadAStarValidation);
	}
	
	Vector2 tmpVector = new Vector2();
	public void update (final float delta) {
		final Array<MoveableEntity> movables = gameWorld.getEntities();
		final Map map = gameWorld.getMap();
		final Blockade[] blockades = gameWorld.getBlockades();
		
		for (int i = 0; i < movables.size; i += 1) {
			final MoveableEntity movable = movables.get(i);
			
			if (movable instanceof Humanoid) {
				Humanoid humanoid = (Humanoid) movable;

				if (humanoid.getState() == Humanoid.State.IDLE) {
					if (humanoid instanceof BruteOrc) {
						final BruteOrc bruteOrc = (BruteOrc) humanoid;
						
						if (bruteOrc.getHealth() <= 0f) {
							if (bruteOrc.getSecondaryTarget() != null) {
								bruteOrc.getSecondaryTarget().setSecondaryTarget(null);
								final City sourceCity = gameWorld.getCities().get(bruteOrc.getSecondaryTarget().getSourceCity());
								sourceCity.addTroop(bruteOrc.getSecondaryTarget());
								
								final int index = movables.indexOf(bruteOrc.getSecondaryTarget(), true);
								movables.removeIndex(index);
								if (index < i) {
									--i;
								}
							}
							
							final Decal decal = new Decal();
							decal.setTexture(gameWorld.getAssets().blood);
							decal.setWidth(gameWorld.getMap().getTileWidth());
							decal.setHeight(gameWorld.getMap().getTileHeight());
							decal.setX(bruteOrc.getX());
							decal.setY(bruteOrc.getY());
							decal.setSpeed(20f);
							gameWorld.getGroundDecals().add(decal);
							
							movables.removeIndex(i);
							--i;
						}
						else {
							bruteOrc.update(delta, gameWorld);
						}
					}
					else if (humanoid instanceof Troop) {
						final Troop troop = (Troop) humanoid;
						if (troop.getHealth() <= 0f) {
							if (troop.getSecondaryTarget() != null) {
								troop.getSecondaryTarget().setSecondaryTarget(null);
								final City sourceCity = gameWorld.getCities().get(troop.getSourceCity());
								sourceCity.defendFrom(troop.getSecondaryTarget());
							}
							
							final Decal decal = new Decal();
							decal.setTexture(gameWorld.getAssets().blood);
							decal.setWidth(gameWorld.getMap().getTileWidth());
							decal.setHeight(gameWorld.getMap().getTileHeight());
							decal.setX(troop.getX());
							decal.setY(troop.getY());
							decal.setSpeed(20f);
							gameWorld.getGroundDecals().add(decal);
							
							movables.removeIndex(i);
							--i;
						}
						else {
							troop.update(delta, gameWorld);
						}
					}
					
					continue;
				}
				
				final float humanoidX = humanoid.getX() + humanoid.getWidth() / 2f;
				final float humanoidY = humanoid.getY() + humanoid.getHeight() / 2f;
				
				final int currentPositionX = (int)(map.getWidth() * (humanoidX / (map.getWidth() * map.getTileWidth())));
				final int currentPositionY = (int)(map.getHeight() * (humanoidY / (map.getHeight() * map.getTileHeight())));

				boolean finish = false;
				
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
								if (!blockades[ii].isActive()) {
									continue;
								}
								
								int tileX = (int)(map.getWidth() * (blockades[ii].getTile().getX() / (map.getWidth() * map.getTileWidth())));
								int tileY = (int)(map.getHeight() * (blockades[ii].getTile().getY() / (map.getHeight() * map.getTileHeight())));
								
								if (tileX == nextX && tileY == nextY) {
									if (humanoid instanceof Worker) {
										Worker worker = (Worker) humanoid;
										final int sourceCity = worker.getSourceCity();
										final City city = gameWorld.getCities().get(sourceCity);
										worker.setResourcesCarried(0);
										worker.setDestinationCity(sourceCity);
										
										final Tile cityTile = city.getCentralTile();
										int cityTileX = (int)(map.getWidth() * (cityTile.getX() / (map.getWidth() * map.getTileWidth())));
										int cityTileY = (int)(map.getHeight() * (cityTile.getY() / (map.getHeight() * map.getTileHeight())));
										
										setupRoadMovement (worker, currentPositionX, currentPositionY, cityTileX, cityTileY);
										
										proceedMovement = false;
									}
									else {
										final BlockadeOrc orc = blockades[ii].getOwner();
										Decal decal = new Decal();
										decal.setTexture(gameWorld.getAssets().blood);
										decal.setWidth(map.getTileWidth());
										decal.setHeight(map.getTileHeight());
										decal.setX(orc.getX());
										decal.setY(orc.getY());
										decal.setSpeed(20f);
										gameWorld.getGroundDecals().add(decal);
										
										decal = new Decal();
										decal.setTexture(gameWorld.getAssets().hit);
										decal.setWidth(map.getTileWidth());
										decal.setHeight(map.getTileHeight());
										decal.setX(orc.getX());
										decal.setY(orc.getY());
										decal.setSpeed(0.3f);
										gameWorld.getDecals().add(decal);
										
										final int orcIndex = movables.indexOf(blockades[ii].getOwner(), true);
										movables.removeIndex(orcIndex);
										if (orcIndex < i) {
											--i;
										}
										
										blockades[ii].setOwner(null);
										blockades[ii].setActive(false);
									}
								}
							}
						}
						
						map.getTile(currentPositionX, currentPositionY).hit();
						humanoid.setLastPosition(currentPositionX, currentPositionY);
						
						if (proceedMovement) {
							humanoid.getWalkPath().clear();
							humanoid.getWalkPath().addAll(
									gameWorld.getAStar().getPath(
											currentPositionX, 
											currentPositionY, 
											humanoid.getDestinationX(), 
											humanoid.getDestinationY(), 
											humanoid.getMovementValidator()));
						}
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
					
					humanoid.setStateTime(humanoid.getStateTime() + delta);
					if (humanoid.getStateTime() > 0.07f) {
						humanoid.setStateTime(0f);
						humanoid.setGoingUp(!humanoid.isGoingUp());
					}
					
					/*float amount = (delta * (humanoid.getHeight() * 1f)) / 0.5f;
					
					if (!humanoid.isGoingUp()) {
						amount *= -1;
					}
					
					humanoid.setUp(humanoid.getUp() + amount);
					if (humanoid.isGoingUp()) {
						if (humanoid.getUp() >= humanoid.getHeight() * 1f) {
							humanoid.setGoingUp(false);
							humanoid.setUp(humanoid.getHeight() * 1f);
						}
					}
					else {
						if (humanoid.getUp() <= 0f) {
							humanoid.setGoingUp(false);
							humanoid.setUp(0);
						}
					}*/
				}
				else {
					finish = true;
				}
				
				if (finish) {
					humanoid.setState(Humanoid.State.IDLE);
					
					if (humanoid instanceof PlayerHumanoid) {
						final PlayerHumanoid playerHumanoid = (PlayerHumanoid) humanoid;
						final int destinationCity = playerHumanoid.getDestinationCity();
						final City city = gameWorld.getCities().get(destinationCity);
						
						if (humanoid instanceof Worker) {
							final Worker worker = (Worker) playerHumanoid;
							final ResourceType resourceType = worker.getType();
							final int resourcesCarried = worker.getResourcesCarried();
							
							switch (resourceType) {
								case WOOD: {
									city.setWoodCount(city.getWoodCount() + resourcesCarried);
									break;
								}
								case IRON: {
									city.setMetalCount(city.getMetalCount() + resourcesCarried);
									break;
								}
								case FOOD: {
									city.setFoodCount(city.getFoodCount() + resourcesCarried);
									break;
								}
								default: {
									break;
								}
							}
							
							city.setWorkerCount(city.getWorkerCount() + 1);
						}
						else if (humanoid instanceof Troop) {
							city.addTroop((Troop) humanoid);
						}
						
						movables.removeIndex(i);
						--i;
					}
					else if (humanoid instanceof Orc) {
						final Orc orc = (Orc) humanoid;
						
						if (orc instanceof BlockadeOrc) {
							final BlockadeOrc blockadeOrc = (BlockadeOrc) orc;
							
							blockadeOrc.getBlockade().setActive(true);
							blockadeOrc.setTexture(gameWorld.getAssets().blockade);
							blockadeOrc.setX(blockadeOrc.getBlockade().getTile().getX());
							blockadeOrc.setY(blockadeOrc.getBlockade().getTile().getY());
						}
						else if (orc instanceof BruteOrc) {
							final BruteOrc orcBrute = (BruteOrc) orc;
							orcBrute.getTarget().defendFrom(orcBrute);
						}
					}
				}
			}
		}
	} 
	
}

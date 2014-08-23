package com.ld30.game.Model.moveable;

import com.badlogic.gdx.utils.Array;
import com.ld30.game.Assets;
import com.ld30.game.Model.GameWorld;
import com.ld30.game.Model.MoveableEntity;

public class MovableManager {

	private GameWorld gameWorld;
	
	public MovableManager (final Assets assets,
						   final GameWorld gameWorld) {
		
		this.gameWorld = gameWorld;
	}
	
	public void update (final float delta) {
		final Array<MoveableEntity> movables = gameWorld.getEntities();
		
		for (int i = 0; i < movables.size; i += 1) {
			final MoveableEntity movable = movables.get(i);
			
			if (movable instanceof Worker) {
				Worker worker = (Worker) movable;
				
				if (worker.getState() == Worker.State.IDLE) {
					continue;
				}
				
				
			}
		}
	} 
	
}

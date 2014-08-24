package com.ld30.game.Model.moveable;

import com.badlogic.gdx.utils.IntArray;
import com.ld30.game.Model.MoveableEntity;
import com.ld30.game.utils.AStar;

public class Humanoid extends MoveableEntity {
	
	public static enum State {
		IDLE,
		WALKING;
	}
	
	private AStar.Validator movementValidator;
	private int lastPositionX;
	private int lastPositionY;
	private State state = State.IDLE;
	private int destinationX;
	private int destinationY;
	private final IntArray walkPath = new IntArray();
	
	public AStar.Validator getMovementValidator() {
		return movementValidator;
	}

	public void setMovementValidator(AStar.Validator movementValidator) {
		this.movementValidator = movementValidator;
	}

	public void setLastPosition (final int lastPositionX, 
			 					 final int lastPositionY) {

		this.lastPositionX = lastPositionX;
		this.lastPositionY = lastPositionY;
	}

	public int getLastPositionX () {
		return lastPositionX;
	}
	
	public int getLastPositonY () {
		return lastPositionY;
	}

	public void setDestination (final int destinationX, 
								final int destinationY) {
		
		this.destinationX = destinationX;
		this.destinationY = destinationY;
	}
	
	public int getDestinationX () {
		return destinationX;
	}
	
	public int getDestinationY () {
		return destinationY;
	}
	
	public void setState (final State state) {
		this.state = state;
	}
	
	public State getState () {
		return state;
	}
	
	public IntArray getWalkPath () {
		return walkPath;
	}
}

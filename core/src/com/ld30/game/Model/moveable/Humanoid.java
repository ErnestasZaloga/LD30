package com.ld30.game.Model.moveable;

import com.badlogic.gdx.utils.IntArray;
import com.ld30.game.Model.MoveableEntity;

public class Humanoid extends MoveableEntity {
	
	public static enum State {
		IDLE,
		WALKING;
	}
	
	private int lastPositionX;
	private int lastPositionY;
	private State state = State.IDLE;
	private int destinationX;
	private int destinationY;
	private final IntArray walkPath = new IntArray();
	
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

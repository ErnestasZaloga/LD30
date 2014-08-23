package com.ld30.game.Model.moveable;

import com.badlogic.gdx.utils.IntArray;
import com.ld30.game.Model.MoveableEntity;

public class Worker extends MoveableEntity {
	
	public static enum State {
		IDLE,
		WALKING;
	}
	
	private State state = State.IDLE;
	private final IntArray walkPath = new IntArray();
	
	public Worker () {}
	
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

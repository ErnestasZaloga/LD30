package com.ld30.game.Model.moveable;


public class Worker extends Humanoid {
	
	private int lastCity;
	
	public Worker () {}
	
	public void setLastCity (final int lastCity) {
		this.lastCity = lastCity;
	}
	
	public int getLastCity () {
		return lastCity;
	}
}

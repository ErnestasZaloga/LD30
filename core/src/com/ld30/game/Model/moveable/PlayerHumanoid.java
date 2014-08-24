package com.ld30.game.Model.moveable;

public abstract class PlayerHumanoid extends Humanoid {
	
	private int sourceCity;
	private int destinationCity;
	
	public int getSourceCity() {
		return sourceCity;
	}

	public void setSourceCity(int sourceCity) {
		this.sourceCity = sourceCity;
	}

	public int getDestinationCity() {
		return destinationCity;
	}

	public void setDestinationCity(int destinationCity) {
		this.destinationCity = destinationCity;
	}
	
}

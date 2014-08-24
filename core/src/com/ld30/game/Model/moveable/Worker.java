package com.ld30.game.Model.moveable;

import com.ld30.game.Model.GameWorld.ResourceType;

public class Worker extends PlayerHumanoid {
	
	private ResourceType type;
	private int resourcesCarried;
	
	public ResourceType getType() {
		return type;
	}
	
	public void setType(ResourceType type) {
		this.type = type;
	}
	
	public int getResourcesCarried() {
		return resourcesCarried;
	}
	
	public void setResourcesCarried(int resourcesCarried) {
		this.resourcesCarried = resourcesCarried;
	}
	
}

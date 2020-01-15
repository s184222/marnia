package com.marnia.entity.registry;

import java.util.UUID;

public class EntityContainer {

	private final float x;
	private final float y;
	
	private final UUID identifier;
	
	public EntityContainer(float x, float y, UUID identifier) {
		this.x = x;
		this.y = y;
		
		this.identifier = identifier;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public UUID getIdentifier() {
		return identifier;
	}
}

package com.marnia.server.world.gen;

import com.marnia.entity.registry.EntityContainer;

public class WorldEntityInfo {

	private final int typeId;
	private final EntityContainer container;
	
	public WorldEntityInfo(int typeId, EntityContainer container) {
		this.typeId = typeId;
		this.container = container;
	}
	
	public int getTypeId() {
		return typeId;
	}
	
	public EntityContainer getContainer() {
		return container;
	}
}

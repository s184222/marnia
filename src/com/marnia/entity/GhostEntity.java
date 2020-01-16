package com.marnia.entity;

import com.marnia.world.MarniaWorld;

import java.util.UUID;

public class GhostEntity extends Entity {

	public GhostEntity(MarniaWorld world, UUID identifier) {
		super(world, identifier);
	}

	@Override
	public void tick() {
		super.tick();
	}
}

package com.marnia.entity;

import com.marnia.server.entity.GhostController;
import com.marnia.world.MarniaWorld;

import java.util.UUID;

public class GhostEntity extends Entity {

	public GhostEntity(MarniaWorld world, UUID identifier) {
		super(world, identifier);

		if (world.isServer())
			setController(new GhostController());
	}

	@Override
	public void tick() {
		super.tick();
	}
}

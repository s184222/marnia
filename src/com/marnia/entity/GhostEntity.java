package com.marnia.entity;

import com.marnia.server.entity.GhostController;
import com.marnia.world.MarniaWorld;

public class GhostEntity extends Entity {

	public GhostEntity(MarniaWorld world) {
		super(world);

		if (world.isServer())
			setController(new GhostController());
	}

	@Override
	public void tick() {
		super.tick();
	}
}

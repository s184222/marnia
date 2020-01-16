package com.marnia.entity.registry;

import com.marnia.entity.GhostEntity;
import com.marnia.world.MarniaWorld;

public class GhostEntityProvider extends BasicEntityProvider<GhostEntity> {

	@Override
	public GhostEntity getEntity(MarniaWorld world, EntityContainer container) {
		GhostEntity ghost = new GhostEntity(world, container.getIdentifier());
		ghost.moveToImmediately(container.getX(), container.getY());
		return ghost;
	}

	@Override
	public Class<GhostEntity> getEntityClass() {
		return GhostEntity.class;
	}
}

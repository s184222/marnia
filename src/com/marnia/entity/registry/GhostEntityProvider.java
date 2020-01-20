package com.marnia.entity.registry;

import com.marnia.entity.GhostEntity;

public class GhostEntityProvider extends BasicEntityProvider<GhostEntity> {

	public GhostEntityProvider() {
		super(GhostEntity::new);
	}

	@Override
	public Class<GhostEntity> getEntityClass() {
		return GhostEntity.class;
	}
}

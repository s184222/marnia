package com.marnia.entity.registry;

import com.marnia.entity.DoorEntity;

public class DoorEntityProvider extends BasicEntityProvider<DoorEntity> {

	public DoorEntityProvider() {
		super(DoorEntity::new);
	}

	@Override
	public Class<DoorEntity> getEntityClass() {
		return DoorEntity.class;
	}
}

package com.marnia.entity.registry;

import com.marnia.entity.KeyEntity;
import com.marnia.world.MarniaWorld;

public class KeyEntityProvider extends BasicEntityProvider<KeyEntity> {

	@Override
	public KeyEntity getEntity(MarniaWorld world, EntityContainer container) {
		KeyEntity keyEntity = new KeyEntity(world, container.getIdentifier());
		keyEntity.moveToImmediately(container.getX(), container.getY());
		return keyEntity;
	}

	@Override
	public Class<KeyEntity> getEntityClass() {
		return KeyEntity.class;
	}
}

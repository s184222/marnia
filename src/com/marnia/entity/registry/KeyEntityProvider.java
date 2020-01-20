package com.marnia.entity.registry;

import com.marnia.entity.KeyEntity;

public class KeyEntityProvider extends BasicEntityProvider<KeyEntity> {

	public KeyEntityProvider() {
		super(KeyEntity::new);
	}

	@Override
	public Class<KeyEntity> getEntityClass() {
		return KeyEntity.class;
	}
}

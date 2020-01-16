package com.marnia.entity.registry;

import com.marnia.entity.PlayerEntity;
import com.marnia.world.MarniaWorld;

public class PlayerEntityProvider extends BasicEntityProvider<PlayerEntity> {

	@Override
	public PlayerEntity getEntity(MarniaWorld world, EntityContainer container) {
		PlayerEntity player = new PlayerEntity(world, container.getIdentifier());
		player.moveToImmediately(container.getX(), container.getY());
		return player;
	}

	@Override
	public Class<PlayerEntity> getEntityClass() {
		return PlayerEntity.class;
	}
}

package com.marnia.entity;

import java.util.UUID;

import com.marnia.server.entity.KeyController;
import com.marnia.world.MarniaWorld;

public class KeyEntity extends Entity {

	private UUID followIdentifier;
	
	public KeyEntity(MarniaWorld world, UUID identifier) {
		super(world, identifier);
	
		followIdentifier = null;
		
		if (world.isServer())
			setController(new KeyController());
	}

	@Override
	public void tick() {
		super.tick();
		
		if (world.isServer()) {
			Entity closestEntity = world.getClosestEntity(this);
			if (closestEntity instanceof PlayerEntity && closestEntity.getHitbox().collides(hitbox)) {
				followIdentifier = closestEntity.identifier;
				((PlayerEntity)closestEntity).pickup(this);
			}
		}
	}
	
	public UUID getFollowIdentifier() {
		return followIdentifier;
	}
}

package com.marnia.entity;

import com.g4mesoft.world.phys.AABB;
import com.marnia.world.MarniaWorld;

public class DoorEntity extends Entity {

	private boolean unlocked;
	
	public DoorEntity(MarniaWorld world) {
		super(world);
	}

	public void setUnlocked(boolean unlocked) {
		this.unlocked = unlocked;
	}
	
	public boolean isUnlocked() {
		return unlocked;
	}
	
	@Override
	protected AABB createHitbox() {
		return new AABB(0.0f, 0.0f, 1.0f, 1.5f);
	}
}

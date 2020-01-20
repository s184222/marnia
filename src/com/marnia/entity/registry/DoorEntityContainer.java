package com.marnia.entity.registry;

import java.util.UUID;

public class DoorEntityContainer extends EntityContainer {

	private final boolean unlocked;
	
	public DoorEntityContainer(float x, float y, UUID identifier, boolean unlocked) {
		super(x, y, identifier);

		this.unlocked = unlocked;
	}

	public boolean isUnlocked() {
		return unlocked;
	}
}

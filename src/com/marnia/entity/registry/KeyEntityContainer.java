package com.marnia.entity.registry;

import java.util.UUID;

public class KeyEntityContainer extends EntityContainer {

	private final UUID followingIdentifier;
	
	public KeyEntityContainer(float x, float y, UUID identifier, UUID followingIdentifier) {
		super(x, y, identifier);
	
		this.followingIdentifier = followingIdentifier;
	}

	public UUID getFollowingIdentifier() {
		return followingIdentifier;
	}
}

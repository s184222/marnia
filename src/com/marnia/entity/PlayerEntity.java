package com.marnia.entity;

import java.util.UUID;

import com.marnia.world.MarniaWorld;

public class PlayerEntity extends Entity {

	public PlayerEntity(MarniaWorld world, UUID identifier) {
		super(world, identifier);
	}
}

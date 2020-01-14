package com.marnia.client.world.entity;

import java.util.UUID;

import com.marnia.entity.IController;
import com.marnia.world.MarniaWorld;

public class ClientPlayer extends DrawableEntity {
	
	public final IController controller;

	public ClientPlayer(MarniaWorld world, UUID identifier, IController controller) {
		super(world, identifier);
		
		this.controller = controller;	
	}
	
	@Override
	public void tick() {
		super.tick();
		
		controller.update(this);
	}
}

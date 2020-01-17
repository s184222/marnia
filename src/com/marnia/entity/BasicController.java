package com.marnia.entity;

public abstract class BasicController implements IController {

	protected static final float FRICTION_X = 0.8f;
	protected static final float FRICTION_Y = 0.8f;
	
	protected static final float WATER_FRICTION_MULTIPLIER = 0.6f;
	
	protected void applyGravity(Entity entity) {
		entity.vel.y += entity.isInWater() ? 0.1f : 0.25f;
	}
	
	protected void applyFriction(Entity entity, float frictionX, float frictionY) {
		if (entity.isInWater()) {
			frictionX *= WATER_FRICTION_MULTIPLIER;
			frictionY *= WATER_FRICTION_MULTIPLIER;
		}

		entity.vel.mul(frictionX, frictionY);
	}
}

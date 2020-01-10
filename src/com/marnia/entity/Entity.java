package com.marnia.entity;

import java.util.List;

import com.g4mesoft.math.MathUtils;
import com.g4mesoft.math.Vec2f;
import com.g4mesoft.world.phys.AABB;
import com.marnia.world.MarniaWorld;

public class Entity {

	public final MarniaWorld world;
	protected final IController controller;
	
	protected final Vec2f prevPos;
	public final Vec2f pos;

	public final Vec2f vel;

	protected final AABB hitbox;

	protected boolean onGround;
	protected boolean hitHorizontal;
	protected boolean hitVertical;
	
	public Entity(MarniaWorld world, IController controller) {
		this.world = world;
		this.controller = controller;
	
		pos = new Vec2f();
		prevPos = new Vec2f();
	
		vel = new Vec2f();
		
		hitbox = new AABB(0.0f, 0.0f, 0.8f, 1.0f);
	}
	
	public void tick() {
		prevPos.set(pos);
		controller.update(this);
	}
	
	public void move() {
		// Ensure we do not move out of bounds.
		vel.x = MathUtils.clamp(vel.x, -hitbox.x0, MarniaWorld.WORLD_WIDTH - hitbox.x1);
		
		List<AABB> hitboxes = world.getCollidingHitboxes(hitbox.expand(vel.x, vel.y));
		
		float moveY = vel.y;
		for (AABB aabb : hitboxes)
			moveY = aabb.clipY(hitbox, moveY);
		hitbox.move(0.0f, moveY);

		float moveX = vel.x;
		for (AABB aabb : hitboxes)
			moveX = aabb.clipX(hitbox, moveX);
		hitbox.move(moveX, 0.0f);

		hitVertical = !MathUtils.nearZero(vel.y - moveY);
		hitHorizontal = !MathUtils.nearZero(vel.x - moveX);

		pos.set(hitbox.x0, hitbox.y0);
		onGround = vel.y > 0.0f && hitVertical;

		if (hitHorizontal)
			vel.x = 0.0f;
		if (hitVertical)
			vel.y = 0.0f;
	}
	
	public float getCenterX() {
		return (hitbox.x0 + hitbox.x1)/2;
	}

	public float getCenterY() {
		return (hitbox.y0 + hitbox.y1)/2;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public boolean hitHorizontalHitbox() {
		return hitHorizontal;
	}

	public boolean hitVerticalHitbox() {
		return hitVertical;
	}
}

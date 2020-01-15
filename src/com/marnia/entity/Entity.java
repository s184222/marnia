package com.marnia.entity;

import java.util.List;
import java.util.UUID;

import com.g4mesoft.math.MathUtils;
import com.g4mesoft.math.Vec2f;
import com.g4mesoft.world.phys.AABB;
import com.marnia.world.MarniaWorld;
import com.marnia.world.tile.Tile;

public class Entity {

	public static final IController DUMMY_CONTROLLER = new DummyController();
	
	public final MarniaWorld world;
	public final UUID identifier;
	private IController controller;
	
	public final Vec2f prevPos;
	public final Vec2f pos;

	public final Vec2f vel;

	protected final AABB hitbox;

	protected boolean onGround;
	protected boolean hitHorizontal;
	protected boolean hitVertical;

	public Entity(MarniaWorld world, UUID identifier) {
		this(world, identifier, DUMMY_CONTROLLER);
	}
	
	public Entity(MarniaWorld world, UUID identifier, IController controller) {
		this.world = world;
		this.identifier = identifier;
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
	
	public void setController(IController controller) {
		if (controller == null)
			throw new IllegalArgumentException("Controller can not be null");
		
		this.controller = controller;
	}
	
	public void move() {
		// Ensure we do not move out of bounds.
		vel.x = MathUtils.clamp(vel.x, -hitbox.x0, world.getWidth() - hitbox.x1);
		
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
	
	public void moveToImmediately(float x, float y) {
		vel.set(x - hitbox.x0, y - hitbox.y0);
		hitbox.move(vel.x, vel.y);
		pos.set(hitbox.x0, hitbox.y0);
	}
	
	public boolean isInWater() {
		int x1 = (int)hitbox.x1;
		int y1 = (int)hitbox.y1;
		
		for (int xt = (int)hitbox.x0; xt <= x1; xt++) {
			for (int yt = (int)hitbox.y0; yt <= y1; yt++) {
				if(world.getTile(xt, yt) == Tile.WATER_TILE)
					return true;
			}
		}
		return false;
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
	
	public AABB getHitbox() {
		return hitbox;
	}
}

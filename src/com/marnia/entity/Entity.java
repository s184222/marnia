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
	protected UUID identifier;
	private IController controller;
	
	public final Vec2f prevPos;
	public final Vec2f pos;

	public final Vec2f vel;

	protected final AABB hitbox;

	protected boolean onGround;
	protected boolean hitHorizontal;
	protected boolean hitVertical;

	private boolean inWater;
	private boolean inWaterNeedsUpdate;
	
	public Entity(MarniaWorld world) {
		this(world, DUMMY_CONTROLLER);
	}
	
	public Entity(MarniaWorld world, IController controller) {
		this.world = world;
		this.controller = controller;
		
		pos = new Vec2f();
		prevPos = new Vec2f();
	
		vel = new Vec2f();
		
		hitbox = createHitbox();
	
		inWater = false;
		inWaterNeedsUpdate = true;
	}
	
	protected AABB createHitbox() {
		return new AABB(0.0f, 0.0f, 0.8f, 1.0f);
	}
	
	public void onAddedToWorld() {
	}

	public void onRemovedFromWorld() {
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
		move(true);
	}
	
	public void move(boolean checkCollision) {
		// Ensure we do not move out of bounds.
		float moveX = vel.x;
		float moveY = vel.y;
		
		if (checkCollision) {
			moveX = MathUtils.clamp(moveX, -hitbox.x0, world.getWidth() - hitbox.x1);
	
			List<AABB> hitboxes = world.getCollidingHitboxes(hitbox.expand(vel.x, vel.y));
	
			for (AABB aabb : hitboxes)
				moveY = aabb.clipY(hitbox, moveY);
			hitbox.move(0.0f, moveY);
	
			for (AABB aabb : hitboxes)
				moveX = aabb.clipX(hitbox, moveX);
			hitbox.move(moveX, 0.0f);
		} else {
			hitbox.move(moveX, moveY);
		}

		hitVertical = !MathUtils.nearZero(vel.y - moveY);
		hitHorizontal = !MathUtils.nearZero(vel.x - moveX);

		pos.set(hitbox.x0, hitbox.y0);
		onGround = vel.y > 0.0f && hitVertical;

		if (hitHorizontal)
			vel.x = 0.0f;
		if (hitVertical)
			vel.y = 0.0f;

		inWaterNeedsUpdate = true;
	}
	
	public void moveToImmediately(float x, float y, boolean placeAtFeet) {
		vel.set(0.0f);
		hitbox.move(x - hitbox.x0, placeAtFeet ? (y - hitbox.y1) : (y - hitbox.y0));
		pos.set(hitbox.x0, hitbox.y0);

		inWaterNeedsUpdate = true;
		
		updateOnGround(0.1f);
	}

	private void updateOnGround(float epsilon) {
		List<AABB> hitboxes = world.getCollidingHitboxes(hitbox.expand(0.0f, epsilon));

		onGround = false;
		for (AABB aabb : hitboxes) {
			if (hitbox.y1 - epsilon < aabb.y0 && hitbox.y1 + epsilon > aabb.y0) {
				onGround = true;
				break;
			}
		}
	}
	
	public boolean isInWater() {
		if (inWaterNeedsUpdate) {
			inWater = calculateInWater();
			inWaterNeedsUpdate = false;
		}
		
		return inWater;
	}
	
	private boolean calculateInWater() {
		int x1 = (int)hitbox.x1;
		int y1 = (int)(hitbox.y1 - 0.1f);
		
		for (int xt = (int)hitbox.x0; xt <= x1; xt++) {
			for (int yt = (int)hitbox.y0; yt <= y1; yt++) {
				if(world.getTile(xt, yt) == Tile.WATER_TILE)
					return true;
			}
		}

		return false;
	}
	
	public void setIdentifier(UUID identifier) {
		if (this.identifier != null)
			throw new IllegalArgumentException("Identifier already set!");
		
		if (identifier != null)
			this.identifier = identifier;
	}
	
	public Tile getTileBelow() {
		int xtMid = (int)(0.5f * (hitbox.x0 + hitbox.x1));
		int ytBelow = (int)(hitbox.y1 + 0.1f);
		return world.getTile(xtMid, ytBelow);
	}
	
	public float getCenterX() {
		return (hitbox.x0 + hitbox.x1) / 2.0f;
	}

	public float getCenterY() {
		return (hitbox.y0 + hitbox.y1) / 2.0f;
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
	
	public UUID getIdentifier() {
		return identifier;
	}
}

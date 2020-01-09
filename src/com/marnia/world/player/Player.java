package com.marnia.world.player;

import java.util.List;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.GColor;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.math.MathUtils;
import com.g4mesoft.math.Vec2f;
import com.g4mesoft.world.phys.AABB;
import com.marnia.util.CameraUtil;
import com.marnia.world.MarniaWorld;

public class Player {
	
	public final MarniaWorld world;
	private final IController controller;
	
	private final Vec2f prevPos;
	public final Vec2f pos;

	public final Vec2f vel;

	private final AABB hitbox;
	private boolean onGround;
	
	public Player(MarniaWorld world, IController controller) {
		this.world = world;
		this.controller = controller;
	
		pos = new Vec2f();
		prevPos = new Vec2f();
	
		vel = new Vec2f();
		
		hitbox = new AABB(0.0f, 0.0f, 1.0f, 1.0f);
	}
	
	public void tick() {
		prevPos.set(pos);
		controller.update(this);
	}
	
	public void move() {
		// Ensure we do not move out of bounds.
		vel.x = MathUtils.clamp(vel.x, -hitbox.x0, MarniaWorld.WORLD_WIDTH - hitbox.x1);
		
		List<AABB> hitboxes = world.getCollidingHitboxes(hitbox.expand(vel.x, vel.y));
		
		float moveX = vel.x;
		for (AABB aabb : hitboxes)
			moveX = aabb.clipX(hitbox, moveX);
		hitbox.move(moveX, 0.0f);
		
		float moveY = vel.y;
		for (AABB aabb : hitboxes)
			moveY = aabb.clipY(hitbox, moveY);
		hitbox.move(0.0f, moveY);

		boolean hitVertical = !MathUtils.nearZero(vel.y - moveY);
		boolean hitHorizontal = !MathUtils.nearZero(vel.x - moveX);

		pos.set(hitbox.x0, hitbox.y0);
		onGround = vel.y > 0.0f && hitVertical;

		if (hitHorizontal)
			vel.x = 0.0f;
		if (hitVertical)
			vel.y = 0.0f;
	}
	
	public void render(IRenderer2D renderer, float dt, DynamicCamera camera) {
		float ix = prevPos.x + (pos.x - prevPos.x) * dt;
		float iy = prevPos.y + (pos.y - prevPos.y) * dt;
		
		int xp = CameraUtil.getPixelX(ix, camera, dt);
		int yp = CameraUtil.getPixelY(iy, camera, dt);
		int w = CameraUtil.getPixelX(ix + hitbox.x1 - hitbox.x0, camera, dt) - xp;
		int h = CameraUtil.getPixelY(iy + hitbox.y1 - hitbox.y0, camera, dt) - yp;
		
		renderer.setColor(GColor.HOT_PINK);
		renderer.fillRect(xp, yp, w, h);
	}
	
	public boolean isOnGround() {
		return onGround;
	}
}

package com.marnia.client.entity.model;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.math.MathUtils;
import com.g4mesoft.world.phys.AABB;
import com.marnia.client.util.CameraUtil;
import com.marnia.client.world.ClientMarniaWorld;
import com.marnia.entity.PlayerEntity;
import com.marnia.graphics.Animation;
import com.marnia.graphics.TextureLoader;

public class PlayerEntityModel extends EntityModel<PlayerEntity> {

	private static final float IDLE_ANIMATION_SPEED = 0.5f;
	private static final float DISTANCE_PER_RUNNING_FRAME = 0.15f;

	private static final float MIN_RUNNING_DISTANCE = 0.1f;

	private static final int JUMP_DOWN_FRAME = 0;
	private static final int JUMP_UP_FRAME = 1;

	private final Animation idleAnimation;
	private final Animation jumpAnimation;
	private final Animation runningAnimation;

	private Animation currentAnimation;

	public PlayerEntityModel(PlayerEntity entity) {
		super(entity);

		TextureLoader tl = ((ClientMarniaWorld)entity.world)
				.getMarniaApp().getTextureLoader();
		
		idleAnimation = new Animation(tl.getPlayerIdleTileSheet(), IDLE_ANIMATION_SPEED);
		jumpAnimation = new Animation(tl.getPlayerJumpTileSheet(), 0.0f);
		runningAnimation = new Animation(tl.getPlayerRunTileSheet());

		currentAnimation = idleAnimation;
	}

	@Override
	public void tick() {
		Animation nextAnimation;
		
		if (entity.isOnGround()) {
			float distX = MathUtils.abs(entity.pos.x - entity.prevPos.x);
			if (distX > MIN_RUNNING_DISTANCE) {
				runningAnimation.setFramesPerTick(distX / DISTANCE_PER_RUNNING_FRAME);
				nextAnimation = runningAnimation;
			} else {
				nextAnimation = idleAnimation;
			}

			if (currentAnimation != nextAnimation)
				nextAnimation.setFrame(0);
		} else {
			jumpAnimation.setFrame((entity.pos.y > entity.prevPos.y) ? JUMP_UP_FRAME : JUMP_DOWN_FRAME);
			nextAnimation = jumpAnimation;
		}

		currentAnimation = nextAnimation;
		currentAnimation.tick();
	}

	@Override
	public void render(IRenderer2D renderer, float dt, DynamicCamera camera) {
		float ix = entity.prevPos.x + (entity.pos.x - entity.prevPos.x) * dt;
		float iy = entity.prevPos.y + (entity.pos.y - entity.prevPos.y) * dt;

		AABB hitbox = entity.getHitbox();

		int yp = CameraUtil.getPixelY(iy, camera, dt);
		int h = CameraUtil.getPixelY(iy + hitbox.y1 - hitbox.y0, camera, dt) - yp;

		int w = currentAnimation.getFrameWidth() * h / currentAnimation.getFrameHeight();
		int xp = CameraUtil.getPixelX(ix + (hitbox.x1 - hitbox.x0) * 0.5f, camera, dt) - w / 2;

		currentAnimation.render(renderer, dt, xp, yp, w, h, (entity.prevPos.x > entity.pos.x));
	}
}

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
import com.marnia.graphics.TileSheet;

public class PlayerEntityModel extends EntityModel<PlayerEntity> {

	private static final float IDLE_ANIMATION_SPEED = 0.5f;
	private static final float DISTANCE_PER_RUNNING_FRAME = 0.2f;

	private static final float MIN_RUNNING_DISTANCE = 0.1f;

	private static final int JUMP_UP_SPRITE_X = 1;
	private static final int JUMP_DOWN_SPRITE_X = 0;

	private final Animation idleAnimation;
	private final TileSheet jumpTileSheet;
	private final Animation runningAnimation;

	private Animation currentAnimation;

	public PlayerEntityModel(PlayerEntity entity) {
		super(entity);

		TextureLoader tl = ((ClientMarniaWorld)entity.world)
				.getMarniaApp().getTextureLoader();
		
		idleAnimation = new Animation(tl.getPlayerIdleTileSheet(), IDLE_ANIMATION_SPEED);
		jumpTileSheet = tl.getPlayerJumpTileSheet();
		runningAnimation = new Animation(tl.getPlayerRunTileSheet());

		currentAnimation = idleAnimation;
	}

	public void tick() {
		float distX = MathUtils.abs(entity.pos.x - entity.prevPos.x);
		if (distX > MIN_RUNNING_DISTANCE) {
			runningAnimation.setFramesPerTick(distX / DISTANCE_PER_RUNNING_FRAME);
			currentAnimation = runningAnimation;
		} else {
			currentAnimation = idleAnimation;
		}
		currentAnimation.tick();
	}

	@Override
	public void render(IRenderer2D renderer, float dt, DynamicCamera camera) {
		float ix = entity.prevPos.x + (entity.pos.x - entity.prevPos.x) * dt;
		float iy = entity.prevPos.y + (entity.pos.y - entity.prevPos.y) * dt;

		AABB hitbox = entity.getHitbox();

		int yp = CameraUtil.getPixelY(iy, camera, dt);
		int h = CameraUtil.getPixelY(iy + hitbox.y1 - hitbox.y0, camera, dt) - yp;

		int tw = entity.isOnGround() ? currentAnimation.getFrameWidth() : jumpTileSheet.getTileWidth();
		int th = entity.isOnGround() ? currentAnimation.getFrameHeight() : jumpTileSheet.getTileHeight();
		
		int w = tw * h / th;
		int xp = CameraUtil.getPixelX(ix + (hitbox.x1 - hitbox.x0) * 0.5f, camera, dt) - w / 2;

		boolean flipX = entity.prevPos.x > entity.pos.x;
		
		if (entity.isOnGround()) {
			currentAnimation.render(renderer, dt, xp, yp, w, h, flipX);
		} else {
			int sx = (entity.pos.y > entity.prevPos.y) ? JUMP_UP_SPRITE_X : JUMP_DOWN_SPRITE_X;
			jumpTileSheet.render(renderer, xp, yp, w, h, sx, 0, flipX);
		}
	}
}

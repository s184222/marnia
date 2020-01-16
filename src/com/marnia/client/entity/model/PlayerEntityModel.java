package com.marnia.client.entity.model;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.GColor;
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

	private static final float IDLE_ANIMATION_SPEED = 1.0f;
	private static final float RUNNING_ANIMATION_SPEED = 1.0f;

	private static final float MIN_RUNNING_DISTANCE = 0.1f;

	private static final int JUMP_UP_SPRITE_X = 0;
	private static final int JUMP_DOWN_SPRITE_X = 1;

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
		runningAnimation = new Animation(tl.getPlayerRunTileSheet(), RUNNING_ANIMATION_SPEED);

		currentAnimation = idleAnimation;
	}

	public void tick() {
		float deltaX = entity.pos.x - entity.prevPos.x;
		if ((MathUtils.abs(deltaX) > MIN_RUNNING_DISTANCE)) {
			// TODO: set playback speed of running animation to depend on deltaX.
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

		int xp = CameraUtil.getPixelX(ix, camera, dt);
		int yp = CameraUtil.getPixelY(iy, camera, dt);
		int w = CameraUtil.getPixelX(ix + hitbox.x1 - hitbox.x0, camera, dt) - xp;
		int h = CameraUtil.getPixelY(iy + hitbox.y1 - hitbox.y0, camera, dt) - yp;

		float deltaY = entity.pos.y - entity.prevPos.y;

		if (entity.isOnGround()) {
			currentAnimation.render(renderer, dt, xp, yp, w, h);
		} else {
			int sx = (deltaY > 0.0f) ? JUMP_UP_SPRITE_X : JUMP_DOWN_SPRITE_X;
			jumpTileSheet.render(renderer, xp, yp, w, h, sx, 0);
		}
	}
}

package com.marnia.client.entity.model;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.math.MathUtils;
import com.marnia.client.world.ClientMarniaWorld;
import com.marnia.entity.PlayerEntity;
import com.marnia.graphics.Animation;
import com.marnia.graphics.TextureLoader;

public class PlayerEntityModel extends EntityModel<PlayerEntity> {

	private static final float IDLE_ANIMATION_SPEED = 0.5f;
	private static final float BLINK_ANIMATION_SPEED = 2.0f;
	private static final float DISTANCE_PER_RUNNING_FRAME = 0.15f;

	private static final float MIN_RUNNING_DISTANCE = 0.1f;
	private static final int RANDOM_BLINK_INTERVAL = 24;
	private static final int IDLE_TIME_FOR_BLINK = 2 * RANDOM_BLINK_INTERVAL;

	private static final int JUMP_DOWN_FRAME = 0;
	private static final int JUMP_UP_FRAME = 1;

	private final Animation idleAnimation;
	private final Animation blinkAnimation;
	private final Animation jumpAnimation;
	private final Animation runningAnimation;
	
	private Animation currentAnimation;

	private int idleTimer;
	
	public PlayerEntityModel(PlayerEntity entity) {
		super(entity);

		TextureLoader tl = ((ClientMarniaWorld)entity.world)
				.getMarniaApp().getTextureLoader();
		
		idleAnimation = new Animation(tl.getPlayerIdleTileSheet(entity.getColor()), IDLE_ANIMATION_SPEED);
		blinkAnimation = new Animation(tl.getPlayerBlinkTileSheet(entity.getColor()), BLINK_ANIMATION_SPEED);
		jumpAnimation = new Animation(tl.getPlayerJumpTileSheet(entity.getColor()), 0.0f);
		runningAnimation = new Animation(tl.getPlayerRunTileSheet(entity.getColor()));

		currentAnimation = idleAnimation;

		blinkAnimation.setFrame(blinkAnimation.getNumFrames() - 1);
		blinkAnimation.setLooping(false);
	}

	@Override
	public void tick() {
		Animation nextAnimation;
		
		if (entity.isOnGround()) {
			float distX = MathUtils.abs(entity.pos.x - entity.prevPos.x);
			if (distX > MIN_RUNNING_DISTANCE) {
				idleTimer = IDLE_TIME_FOR_BLINK;
				
				runningAnimation.setFramesPerTick(distX / DISTANCE_PER_RUNNING_FRAME);
				nextAnimation = runningAnimation;
			} else {
				if (idleTimer > 0)
					idleTimer--;
				
				if (!blinkAnimation.hasFinished() || idleTimer <= 0) {
					idleTimer = IDLE_TIME_FOR_BLINK + RANDOM_BLINK_INTERVAL * entity.world.random.nextInt(5);
					nextAnimation = blinkAnimation;
				} else {
					nextAnimation = idleAnimation;
				}
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
		boolean flipX = (entity.prevPos.x - entity.pos.x > MathUtils.EPSILON);
		renderAnimation(renderer, dt, camera, currentAnimation, 0.0f, 0.0f, flipX);

	}
}

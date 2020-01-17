package com.marnia.client.entity.model;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.math.MathUtils;
import com.marnia.client.world.ClientMarniaWorld;
import com.marnia.entity.Entity;
import com.marnia.entity.GhostEntity;
import com.marnia.graphics.Animation;
import com.marnia.graphics.TextureLoader;

public class GhostEntityModel extends EntityModel<GhostEntity> {

	private static final float SCARE_DIST_SQR = 5.0f;
	
	private static final int NORMAL_FRAME = 0;
	private static final int SCARE_FRAME = 1;
	
	private static final float FLOATING_SPEED = 0.2f;
	private static final float FLOATING_AMPLITUDE = 0.1f;
	
	private final Animation ghostAnimation;

	private float prevFloatingTimer;
	private float floatingTimer;

	public GhostEntityModel(GhostEntity entity) {
		super(entity);

		TextureLoader tl = ((ClientMarniaWorld)entity.world)
				.getMarniaApp().getTextureLoader();

		ghostAnimation = new Animation(tl.getGhostTileSheet(), 0.0f);
	}

	@Override
	public void tick() {
		Entity closestEntity = entity.world.getClosestEntity(entity);
		float distX = closestEntity.getCenterX() - entity.getCenterX();
		float distY = closestEntity.getCenterY() - entity.getCenterY();
		
		float distSqr = distX * distX + distY + distY;

		ghostAnimation.setFrame((distSqr < SCARE_DIST_SQR) ? SCARE_FRAME : NORMAL_FRAME);
	
		prevFloatingTimer = floatingTimer;
		floatingTimer += FLOATING_SPEED;
		floatingTimer %= (2.0f * MathUtils.PI);
	}

	@Override
	public void render(IRenderer2D renderer, float dt, DynamicCamera camera) {
		boolean flipX = (entity.pos.x - entity.prevPos.x > MathUtils.EPSILON);

		float floatingTimer = (prevFloatingTimer + FLOATING_SPEED * dt) % (2.0f * MathUtils.PI);
		float floatingOffset = FLOATING_AMPLITUDE * (MathUtils.cos(floatingTimer) * 0.5f + 1.0f);
		renderAnimation(renderer, dt, camera, ghostAnimation, 0.0f, floatingOffset, flipX);
	}
}

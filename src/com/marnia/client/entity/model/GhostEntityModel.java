package com.marnia.client.entity.model;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.math.MathUtils;
import com.g4mesoft.world.phys.AABB;
import com.marnia.client.util.CameraUtil;
import com.marnia.client.world.ClientMarniaWorld;
import com.marnia.entity.Entity;
import com.marnia.entity.GhostEntity;
import com.marnia.graphics.Animation;
import com.marnia.graphics.TextureLoader;

public class GhostEntityModel extends EntityModel<GhostEntity> {

	private static final float SCARE_DIST_SQR = 5.0f;
	
	private static final int NORMAL_FRAME = 0;
	private static final int SCARE_FRAME = 1;
	
	private final Animation ghostAnimation;

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
	}

	@Override
	public void render(IRenderer2D renderer, float dt, DynamicCamera camera) {
		float ix = entity.prevPos.x + (entity.pos.x - entity.prevPos.x) * dt;
		float iy = entity.prevPos.y + (entity.pos.y - entity.prevPos.y) * dt;

		AABB hitbox = entity.getHitbox();

		int yp = CameraUtil.getPixelY(iy, camera, dt);
		int h = CameraUtil.getPixelY(iy + hitbox.y1 - hitbox.y0, camera, dt) - yp;

		int w = ghostAnimation.getFrameWidth() * h / ghostAnimation.getFrameHeight();
		int xp = CameraUtil.getPixelX(ix + (hitbox.x1 - hitbox.x0) * 0.5f, camera, dt) - w / 2;

		boolean flipX = (entity.prevPos.x - entity.pos.x > MathUtils.EPSILON);
		ghostAnimation.render(renderer, dt, xp, yp, w, h, !flipX);
	}
}

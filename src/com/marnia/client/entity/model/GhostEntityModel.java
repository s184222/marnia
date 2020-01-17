package com.marnia.client.entity.model;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.math.MathUtils;
import com.g4mesoft.world.phys.AABB;
import com.marnia.client.util.CameraUtil;
import com.marnia.client.world.ClientMarniaWorld;
import com.marnia.entity.GhostEntity;
import com.marnia.graphics.Animation;
import com.marnia.graphics.TextureLoader;

public class GhostEntityModel extends EntityModel<GhostEntity> {

	private final Animation ghostAnimation;

	public GhostEntityModel(GhostEntity entity) {
		super(entity);

		TextureLoader tl = ((ClientMarniaWorld)entity.world)
				.getMarniaApp().getTextureLoader();

		ghostAnimation = new Animation(tl.getGhostTileSheet(), 0.0f);
		ghostAnimation.setFrame(0);
	}

	@Override
	public void tick() {
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

		boolean flipX = (entity.prevPos.x - entity.pos.x > MathUtils.EPSILON);
		ghostAnimation.render(renderer, dt, xp, yp, w, h, !flipX);
	}
}

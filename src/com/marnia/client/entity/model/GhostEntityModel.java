package com.marnia.client.entity.model;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.GColor;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.world.phys.AABB;
import com.marnia.client.util.CameraUtil;
import com.marnia.entity.GhostEntity;

public class GhostEntityModel extends EntityModel<GhostEntity> {

	public GhostEntityModel(GhostEntity entity) {
		super(entity);
	}

	@Override
	public void tick() {
		entity.pos.x +=0.1;
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

		renderer.setColor(GColor.BLACK);
		renderer.fillRect(xp,yp, w, h);
	}
}

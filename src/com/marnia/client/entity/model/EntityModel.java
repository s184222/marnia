package com.marnia.client.entity.model;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.world.phys.AABB;
import com.marnia.client.util.CameraUtil;
import com.marnia.entity.Entity;
import com.marnia.graphics.Animation;
import com.marnia.world.WorldTheme;

public abstract class EntityModel<E extends Entity> {

	protected final E entity;

	public EntityModel(E entity) {
		this.entity = entity;
	}

	public void onThemeChanged(WorldTheme theme) {
	}
	
	public abstract void tick();

	public abstract void render(IRenderer2D renderer, float dt, DynamicCamera camera);

	protected void renderAnimation(IRenderer2D renderer, float dt, DynamicCamera camera, Animation animation, float xo, float yo, boolean flipX) {
		AABB hitbox = entity.getHitbox();

		float ix = entity.prevPos.x + (entity.pos.x - entity.prevPos.x) * dt - xo;
		float iy = entity.prevPos.y + (entity.pos.y - entity.prevPos.y) * dt - yo;

		float hh = hitbox.y1 - hitbox.y0;
		int h = CameraUtil.getScaledSize(hh, camera, dt);
		int yp = CameraUtil.getPixelY(iy + hh, camera, dt) - h;

		int w = animation.getFrameWidth() * h / animation.getFrameHeight();
		int xp = CameraUtil.getPixelX(ix + (hitbox.x1 - hitbox.x0) * 0.5f, camera, dt) - w / 2;

		if (xp + w >= 0 && xp < renderer.getWidth() && yp + h >= 0 && yp < renderer.getHeight())
			animation.render(renderer, dt, xp, yp, w, h, flipX);
	}
	
	public E getEntity() {
		return entity;
	}
}

package com.marnia.client.entity.model;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.math.MathUtils;
import com.marnia.client.world.ClientMarniaWorld;
import com.marnia.entity.KeyEntity;
import com.marnia.graphics.Animation;
import com.marnia.graphics.TextureLoader;

public class KeyEntityModel extends EntityModel<KeyEntity> {

	private static final float FLOATING_SPEED = 0.1f;
	private static final float FLOATING_AMPLITUDE = 0.15f;
	
	private final Animation keyAnimation;
	
	private float prevFloatingTimer;
	private float floatingTimer;
	
	public KeyEntityModel(KeyEntity entity) {
		super(entity);
		
		TextureLoader tl = ((ClientMarniaWorld)entity.world)
				.getMarniaApp().getTextureLoader();
		
		keyAnimation = new Animation(tl.getKeyTileSheet(), 0.0f);
	}

	@Override
	public void tick() {
		prevFloatingTimer = floatingTimer;
		floatingTimer += FLOATING_SPEED;
		floatingTimer %= (2.0f * MathUtils.PI);
	}

	@Override
	public void render(IRenderer2D renderer, float dt, DynamicCamera camera) {
		float floatingTimer = (prevFloatingTimer + FLOATING_SPEED * dt) % (2.0f * MathUtils.PI);
		float floatingOffset = FLOATING_AMPLITUDE * MathUtils.cos(floatingTimer);
		renderAnimation(renderer, dt, camera, keyAnimation, 0.0f, floatingOffset, false);
	}
}

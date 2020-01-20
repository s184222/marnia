package com.marnia.client.entity.model;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.IRenderer2D;
import com.marnia.client.world.ClientMarniaWorld;
import com.marnia.entity.DoorEntity;
import com.marnia.graphics.Animation;
import com.marnia.graphics.TextureLoader;

public class DoorEntityModel extends EntityModel<DoorEntity> {

	private final Animation doorAnimation;
	
	public DoorEntityModel(DoorEntity entity) {
		super(entity);
	
		TextureLoader tl = ((ClientMarniaWorld)entity.world)
				.getMarniaApp().getTextureLoader();
		
		doorAnimation = new Animation(tl.getDoorTileSheet(), 0.0f);
	}

	@Override
	public void tick() {
	}

	@Override
	public void render(IRenderer2D renderer, float dt, DynamicCamera camera) {
		renderAnimation(renderer, dt, camera, doorAnimation, 0.0f, 0.0f, false);
	}
}

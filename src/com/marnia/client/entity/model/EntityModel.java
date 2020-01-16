package com.marnia.client.entity.model;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.IRenderer2D;
import com.marnia.entity.Entity;

public abstract class EntityModel<E extends Entity> {

	protected final E entity;

	public EntityModel(E entity) {
		this.entity = entity;
	}

	public abstract void tick();

	public abstract void render(IRenderer2D renderer, float dt, DynamicCamera camera);

	public E getEntity() {
		return entity;
	}
}

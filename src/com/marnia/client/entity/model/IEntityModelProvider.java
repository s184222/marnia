package com.marnia.client.entity.model;

import com.marnia.entity.Entity;

public interface IEntityModelProvider<E extends Entity> {

	public EntityModel<E> getEntityModel(E entity);

}

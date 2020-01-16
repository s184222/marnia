package com.marnia.client.entity.model;

import com.marnia.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class EntityModelRegistry {

	private final Map<Class<? extends Entity>, IEntityModelProvider<?>> modelProviders;

	public EntityModelRegistry() {
		modelProviders = new HashMap<Class<? extends Entity>, IEntityModelProvider<?>>();
	}

	public <E extends Entity> void registerModelProvider(Class<E> entityClazz, IEntityModelProvider<E> provider) {
		if (modelProviders.containsKey(entityClazz))
			throw new IllegalArgumentException("Entity class already has a registered model: " + entityClazz);
		modelProviders.put(entityClazz, provider);
	}

	@SuppressWarnings("unchecked")
	public <E extends Entity> EntityModel<E> getEntityModel(E entity) {
		Class<? extends Entity> entityClazz = entity.getClass();
		if (!modelProviders.containsKey(entityClazz))
			throw new IllegalArgumentException("Entity does not have a registered model: " + entity.getClass());
		return ((IEntityModelProvider<E>)modelProviders.get(entityClazz)).getEntityModel(entity);
	}
}

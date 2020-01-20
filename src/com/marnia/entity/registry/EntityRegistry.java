package com.marnia.entity.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.marnia.entity.Entity;
import com.marnia.world.MarniaWorld;

public class EntityRegistry {

	public static final int PLAYER_PROVIDER_ID = 0;
	public static final int GHOST_PROVIDER_ID = 1;
	public static final int KEY_PROVIDER_ID = 2;
	public static final int DOOR_PROVIDER_ID = 3;

	private static EntityRegistry instance;
	
	private final Map<Integer, IEntityProvider<?, ?>> idToEntityProvider;
	private final Map<Class<? extends Entity>, Integer> entityToId;
	
	public EntityRegistry() {
		idToEntityProvider = new ConcurrentHashMap<Integer, IEntityProvider<?, ?>>();
		entityToId = new ConcurrentHashMap<Class<? extends Entity>, Integer>();
	}
	
	public void registerEntityProvider(IEntityProvider<?, ?> provider, int id) {
		if (idToEntityProvider.containsKey(id))
			throw new IllegalArgumentException("Id is already registered.");
		if (entityToId.containsKey(provider.getEntityClass()))
			throw new IllegalArgumentException("Id is already registered.");
		
		idToEntityProvider.put(id, provider);
		entityToId.put(provider.getEntityClass(), id);
	}
	
	public IEntityProvider<?, ?> getEntityProvider(int id) {
		return idToEntityProvider.get(id);
	}
	
	@SuppressWarnings("unchecked")
	public Entity getEntity(int id, MarniaWorld world, EntityContainer container, boolean placeAtFeet) {
		@SuppressWarnings("rawtypes")
		IEntityProvider entityProvider = getEntityProvider(id);
		if (entityProvider == null)
			return null;
		if (!entityProvider.getContainerClass().equals(container.getClass()))
			return null;
		return entityProvider.getEntity(world, container, placeAtFeet);
	}
	
	public int getEntityId(Class<? extends Entity> entityClazz) {
		Integer result = entityToId.get(entityClazz);
		if (result == null)
			throw new IllegalArgumentException("Entity class " + entityClazz + " is not registered.");
		return result.intValue();
	}

	public int getEntityId(Entity entity) {
		return getEntityId(entity.getClass());
	}
	
	private static void registerEntityProviders() {
		instance.registerEntityProvider(new PlayerEntityProvider(), PLAYER_PROVIDER_ID);
		instance.registerEntityProvider(new GhostEntityProvider(), GHOST_PROVIDER_ID);
		instance.registerEntityProvider(new KeyEntityProvider(), KEY_PROVIDER_ID);
		instance.registerEntityProvider(new DoorEntityProvider(), DOOR_PROVIDER_ID);
	}
	
	public static synchronized EntityRegistry getInstance() {
		if (instance == null) {
			instance = new EntityRegistry();
			registerEntityProviders();
		}
		
		return instance;
	}
}

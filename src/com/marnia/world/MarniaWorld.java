package com.marnia.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.g4mesoft.world.phys.AABB;
import com.marnia.decorations.Decoration;
import com.marnia.entity.Entity;
import com.marnia.world.tile.Tile;

public abstract class MarniaWorld {

	private WorldTheme theme;
	protected final WorldStorage storage;

	protected final List<Entity> entities;
	private final List<Entity> entitiesToRemove;
	private final List<Entity> entitiesToAdd;
	
	protected final Map<UUID, Entity> identifierToEntity;
	
	protected final List<Decoration> decorations;
	
	private boolean updatingEntities;

	public final Random random;
	
	public MarniaWorld(WorldTheme theme) {
		this.theme = theme;
		
		storage = new WorldStorage(0, 0);
	
		entities = new ArrayList<Entity>();
		entitiesToRemove = new ArrayList<Entity>();
		entitiesToAdd = new ArrayList<Entity>();
		
		identifierToEntity = new LinkedHashMap<UUID, Entity>();
		
		decorations = new ArrayList<Decoration>();
	
		random = new Random();
	}
	
	public void setTile(int xt, int yt, Tile tile) {
		storage.setTileIndex(xt, yt, tile.getIndex());
	}

	public Tile getTile(int xt, int yt) {
		return Tile.getTile(storage.getTileIndex(xt, yt));
	}

	public boolean isAir(int xt, int yt) {
		return getTile(xt, yt) == Tile.AIR_TILE;
	}

	public void tick() {
		updatingEntities = true;
		for (Entity entity : entities)
			entity.tick();
		updatingEntities = false;
		
		for (Entity entity : entitiesToAdd)
			addEntity(entity);
		for (Entity entity : entitiesToRemove)
			removeEntity(entity);
	}
	
	public void addEntity(Entity entity) {
		UUID identifier = entity.getIdentifier();
		if (identifier == null) {
			identifier = createUniqueIdentifier();
			entity.setIdentifier(identifier);
		}
		
		if (updatingEntities) {
			entitiesToAdd.add(entity);
		} else {
			entities.add(entity);
			entity.onAddedToWorld();
		}

		identifierToEntity.put(identifier, entity);
	}
	
	private UUID createUniqueIdentifier() {
		UUID identifier;
		do {
			identifier = UUID.randomUUID();
		} while (getEntity(identifier) != null);
		
		return identifier;
	}
	
	public boolean removeEntity(Entity entity) {
		if (!updatingEntities) {
			if (entities.remove(entity)) {
				entity.onRemovedFromWorld();
				identifierToEntity.remove(entity.getIdentifier());
				return true;
			}
		} else if (entities.contains(entity) || entitiesToAdd.contains(entity)) {
			entitiesToRemove.add(entity);
			return true;
		}
		
		return false;
	}

	public Entity getEntity(UUID identifier) {
		return identifierToEntity.get(identifier);
	}
	
	public List<AABB> getCollidingHitboxes(AABB hitbox) {
		List<AABB> hitboxes = new ArrayList<AABB>();
		
		int x1 = (int)hitbox.x1;
		int y1 = (int)hitbox.y1;
		for (int xt = (int)hitbox.x0; xt <= x1; xt++)
			for (int yt = (int)hitbox.y0; yt <= y1; yt++)
				getTile(xt, yt).getHitboxes(this, xt, yt, hitboxes);

		Iterator<AABB> itr = hitboxes.iterator();
		while (itr.hasNext()) {
			if (!itr.next().collides(hitbox))
				itr.remove();
		}

		return hitboxes;
	}
	
	public Entity getClosestEntity(Entity entity) {
		float minimumDist = Float.POSITIVE_INFINITY;
		Entity closestEntity = null;
		
		for (Entity other : entities) {
			if (other != entity) {
				float distX = other.getCenterX() - entity.getCenterX();
				float distY = other.getCenterY() - entity.getCenterY();
				
				float dist = distX * distX + distY * distY;

				if (dist < minimumDist) {
					minimumDist = dist;
					closestEntity = other;
				}
			}
		}
		
		return closestEntity;
	}
	
	public void clearWorld() {
		setWorldStorage(new WorldStorage(0, 0));
		
		entities.clear();
		entitiesToAdd.clear();
		entitiesToRemove.clear();
		identifierToEntity.clear();
		decorations.clear();
	}

	public void setTheme(WorldTheme theme) {
		this.theme = theme;
	}
	
	public WorldTheme getTheme() {
		return theme;
	}
	
	public void addDecoration(Decoration decoration) {
		decorations.add(decoration);
	}

	public List<Decoration> getDecorations() {
		return Collections.unmodifiableList(decorations);
	}
	
	public List<Entity> getEntities() {
		return Collections.unmodifiableList(entities);
	}
	
	public int getWidth() {
		return storage.getWidth();
	}

	public int getHeight() {
		return storage.getHeight();
	}
	
	public void setWorldStorage(WorldStorage storage) {
		this.storage.copy(storage);
	}
	
	public WorldStorage getStorage() {
		return storage;
	}

	public abstract boolean isServer();
}

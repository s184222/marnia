package com.marnia.world;

import java.util.ArrayList;
import java.util.List;

import com.g4mesoft.world.phys.AABB;
import com.marnia.entity.Entity;
import com.marnia.world.tile.Tile;

public abstract class MarniaWorld {

	protected final WorldStorage storage;

	private final List<Entity> entities;
	private final List<Entity> entitiesToRemove;
	private final List<Entity> entitiesToAdd;
	
	private boolean updatingEntities;
	
	public MarniaWorld() {
		storage = new WorldStorage(0, 0);
	
		entities = new ArrayList<Entity>();
		entitiesToRemove = new ArrayList<Entity>();
		entitiesToAdd = new ArrayList<Entity>();
	}
	
	public void setTile(int xt, int yt, Tile tile) {
		storage.setTileIndex(xt, yt, tile.getIndex());
	}

	public Tile getTile(int xt, int yt) {
		return Tile.getTile(storage.getTileIndex(xt, yt));
	}
	
	public void tick() {
		updatingEntities = true;
		for (Entity entity : entities)
			entity.tick();
		updatingEntities = false;
		
		for (Entity entity : entitiesToAdd)
			entities.add(entity);
		for (Entity entity : entitiesToRemove)
			entities.remove(entity);
	}
	
	public void addEntity(Entity entity) {
		if (updatingEntities) {
			entitiesToAdd.add(entity);
		} else {
			entities.add(entity);
		}
	}
	
	public void removeEntity(Entity entity) {
		if (updatingEntities) {
			entitiesToRemove.add(entity);
		} else {
			entities.remove(entity);
		}
	}
	
	public List<AABB> getCollidingHitboxes(AABB hitbox) {
		List<AABB> hitboxes = new ArrayList<AABB>();
		
		int x1 = (int)hitbox.x1;
		int y1 = (int)hitbox.y1;
		for (int xt = (int)hitbox.x0; xt <= x1; xt++)
			for (int yt = (int)hitbox.y0; yt <= y1; yt++)
				getTile(xt, yt).getHitboxes(this, xt, yt, hitboxes);
		
		return hitboxes;
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
}

package com.marnia.world;

import java.util.ArrayList;
import java.util.List;

import com.g4mesoft.world.phys.AABB;
import com.marnia.world.tile.Tile;

public abstract class MarniaWorld {

	public static final int WORLD_WIDTH = 100;
	public static final int WORLD_HEIGHT = 20;
	
	private final int[] tiles;
	
	public MarniaWorld() {
		tiles = new int[WORLD_WIDTH * WORLD_HEIGHT];
		
		for (int xt = 0; xt < WORLD_WIDTH; xt++)
			setTile(xt, WORLD_HEIGHT - 1, Tile.SOLID_TILE);

		for (int xt = 5; xt < 10; xt++)
			setTile(xt, 10, Tile.SOLID_TILE);
		for(int xt = 9; xt < 14; xt++)
			setTile(xt, 14 - (xt - 9), Tile.SOLID_TILE);
		for (int xt = 20; xt < 25; xt++)
			setTile(xt, 5, Tile.SOLID_TILE);
		for (int xt = 30; xt < 35; xt++)
			setTile(xt, 1, Tile.SOLID_TILE);
	}
	
	public boolean isInBounds(int xt, int yt) {
		return xt >= 0 && xt < WORLD_WIDTH && yt >= 0 && yt < WORLD_HEIGHT;
	}
	
	public void setTile(int xt, int yt, Tile tile) {
		if (isInBounds(xt, yt))
			tiles[xt + yt * WORLD_WIDTH] = tile.getIndex();
	}

	public Tile getTile(int xt, int yt) {
		if (isInBounds(xt, yt))
			return Tile.getTile(tiles[xt + yt * WORLD_WIDTH]);
		return Tile.AIR_TILE;
	}
	
	public void tick() {
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
}

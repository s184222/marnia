package com.marnia.world.tile;

import java.util.List;

import com.g4mesoft.world.phys.AABB;
import com.marnia.world.MarniaWorld;

public class Tile {
	
	private static final Tile[] tiles = new Tile[3];

	public static final Tile AIR_TILE = new Tile(0);
	public static final Tile SOLID_TILE = new EarthTile(1);
	public static final Tile SMALL_GRASS_TILE = new SmallGrassTile(2);

	private final int index;
	
	public Tile(int tileIndex) {
		if (tiles[tileIndex] != null)
			throw new IllegalArgumentException("Tile with index" + tileIndex + "already exists");

		index = tileIndex;
		
		tiles[tileIndex] = this;
	}
	
	public int getIndex() {
		return index;
	}

	public static Tile getTile(int index) {
		return (index < 0 || index >= tiles.length) ? AIR_TILE : tiles[index];
	}

	public void getHitboxes(MarniaWorld world, int xt, int yt, List<AABB> hitboxes) {
	}

	public byte getSpriteData(MarniaWorld world, int xt, int yt) {
		return 0;
	}
}

package com.marnia.world.tile;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.world.phys.AABB;
import com.marnia.world.MarniaWorld;

public class Tile {
	
	private static final Tile[] tiles = new Tile[2];

	public static final Tile AIR_TILE = new Tile(0);
	public static final Tile SOLID_TILE = new SolidTile(1);

	private final int index;
	
	public Tile(int tileIndex) {
		if (tiles[tileIndex] != null)
			throw new IllegalArgumentException("Tile with index" + tileIndex + "already exists");

		index = tileIndex;
		
		tiles[tileIndex] = this;
	}
	
	public void render(MarniaWorld world, int xt, int yt, IRenderer2D renderer, float dt, DynamicCamera camera) {
	}

	public AABB getHitbox(MarniaWorld world, int xt, int yt) {
		return new AABB(xt, yt, xt + 1.0f, yt + 1.0f);
	}
	
	public int getIndex() {
		return index;
	}

	public static Tile getTile(int index) {
		return (index < 0 || index >= tiles.length) ? AIR_TILE : tiles[index];
	}
}

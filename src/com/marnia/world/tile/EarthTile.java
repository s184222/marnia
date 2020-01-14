package com.marnia.world.tile;

import java.util.List;

import com.g4mesoft.world.phys.AABB;
import com.marnia.util.SpriteHelper;
import com.marnia.world.MarniaWorld;

public class EarthTile extends Tile {

	public EarthTile(int tileIndex) {
		super(tileIndex);
	}

	@Override
	public void getHitboxes(MarniaWorld world, int xt, int yt, List<AABB> hitboxes) {
		hitboxes.add(new AABB(xt, yt, xt + 1.0f, yt + 1.0f));
	}
	
	@Override
	public byte getSpriteData(MarniaWorld world, int xt, int yt) {
		int sy = 0;
		int sx = 0;

		if (world.getTile(xt, yt - 1) != Tile.AIR_TILE)
			sy += 2;
		if (world.getTile(xt, yt + 1) != Tile.AIR_TILE)
			sy++;

		if (world.getTile(xt + 1, yt) != Tile.AIR_TILE) {
			sx++;
			if ((world.getTile(xt + 1, yt - 1) != Tile.AIR_TILE))
				sx++;
		}

		if (world.getTile(xt - 1, yt) != Tile.AIR_TILE) {
			sx += 3;
			if (world.getTile(xt - 1, yt - 1) != Tile.AIR_TILE)
				sx += 3;
		}


		return SpriteHelper.getSpriteDataAt(sx, sy);
	}
}

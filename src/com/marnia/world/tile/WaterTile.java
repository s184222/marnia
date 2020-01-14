package com.marnia.world.tile;

import com.marnia.util.SpriteHelper;
import com.marnia.world.MarniaWorld;

public class WaterTile extends Tile {

	public WaterTile(int tileIndex) {
		super(tileIndex);
	}

	@Override
	public byte getSpriteData(MarniaWorld world, int xt, int yt) {
		int sy = (world.getTile(xt, yt -1) != Tile.AIR_TILE) ? 5 : 4;
		return SpriteHelper.getSpriteDataAt(4, sy);
	}
}

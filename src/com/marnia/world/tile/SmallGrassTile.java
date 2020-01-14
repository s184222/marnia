package com.marnia.world.tile;

import java.util.List;

import com.g4mesoft.world.phys.AABB;
import com.marnia.util.SpriteHelper;
import com.marnia.world.MarniaWorld;

public class SmallGrassTile extends Tile {

	public SmallGrassTile(int tileIndex) {
		super(tileIndex);
	}

	@Override
	public void getHitboxes(MarniaWorld world, int xt, int yt, List<AABB> hitboxes) {
		hitboxes.add(new AABB(xt, yt, xt + 1.0f, yt + 0.7f));
	}
	
	@Override
	public byte getSpriteData(MarniaWorld world, int xt, int yt) {
		int sx = 0;
		
		if (world.getTile(xt + 1, yt) != Tile.AIR_TILE) 
			sx++;
		
		if (world.getTile(xt - 1, yt) != Tile.AIR_TILE) 
			sx += 2;
		
		
		return SpriteHelper.getSpriteDataAt(sx, 4);
	}
}

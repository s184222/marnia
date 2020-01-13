package com.marnia.world.tile;

import java.util.List;

import com.g4mesoft.world.phys.AABB;
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
		return 0;
	}
}

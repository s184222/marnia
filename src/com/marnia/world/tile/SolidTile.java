package com.marnia.world.tile;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.GColor;
import com.g4mesoft.graphic.IRenderer2D;
import com.marnia.world.MarniaWorld;

public class SolidTile extends Tile {

	public SolidTile(int tileIndex) {
		super(tileIndex);
	}

	@Override
	public void render(MarniaWorld world, int xt, int yt, IRenderer2D renderer, float dt, DynamicCamera camera) {
		float s = camera.getScale(dt);

		float rx = (xt - camera.getXOffset(dt));
		float ry = (yt - camera.getYOffset(dt));
		
		int xp = (int)(rx * s * MarniaWorld.TILE_SIZE);
		int yp = (int)(ry * s * MarniaWorld.TILE_SIZE);
		int w = (int)((rx + 1.0f) * s * MarniaWorld.TILE_SIZE) - xp;
		int h = (int)((ry + 1.0f) * s * MarniaWorld.TILE_SIZE) - yp;
		
		renderer.setColor(GColor.AQUAMARINE);
		renderer.fillRect((int)xp, (int)yp, w, h);
	}
}

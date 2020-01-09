package com.marnia.world.tile;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.GColor;
import com.g4mesoft.graphic.IRenderer2D;
import com.marnia.util.CameraUtil;
import com.marnia.world.MarniaWorld;

public class SolidTile extends Tile {

	public SolidTile(int tileIndex) {
		super(tileIndex);
	}

	@Override
	public void render(MarniaWorld world, int xt, int yt, IRenderer2D renderer, float dt, DynamicCamera camera) {
		float s = camera.getScale(dt);

		int xp = CameraUtil.getPixelX(xt, camera, dt);
		int yp = CameraUtil.getPixelY(yt, camera, dt);
		int w = CameraUtil.getPixelX(xt + 1.0f, camera, dt) - xp;
		int h = CameraUtil.getPixelY(yt + 1.0f, camera, dt) - yp;
		
		renderer.setColor(GColor.AQUAMARINE);
		renderer.fillRect(xp, yp, w, h);
	}
}

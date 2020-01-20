package com.marnia.client.world;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.IRenderer2D;
import com.marnia.client.util.CameraUtil;
import com.marnia.graphics.Texture;

public class ParallaxedWorldTexture {

	private final ClientMarniaWorld world;
	private final Texture backgroundLayer;
	private final float parallaxingFactor;
	
	public ParallaxedWorldTexture(ClientMarniaWorld world, Texture backgroundLayer, float parallaxingFactor) {
		this.world = world;
		this.backgroundLayer = backgroundLayer;
		this.parallaxingFactor = parallaxingFactor;
	}
	
	public void render(IRenderer2D renderer, float dt, DynamicCamera camera) {
		float scale = camera.getScale(dt);
		float vw = camera.getScreenWidth() / scale;
		float vh = camera.getScreenHeight() / scale;

		float ww = (float)world.getWidth();
		float wh = (float)world.getHeight();
		
		float aspect = (float)backgroundLayer.getWidth() / backgroundLayer.getHeight();
		float bgh = vh + (wh - vh) * parallaxingFactor;
		float bgw = bgh * aspect;

		float defaultScale = world.getMarniaApp().getDefaultCameraScale();
		float dvw = camera.getScreenWidth() / defaultScale;
		float dvh = camera.getScreenHeight() / defaultScale;
		float numTiles = (parallaxingFactor * (ww - dvw) + dvw) /
				(aspect * (parallaxingFactor * (wh - dvh) + dvh));
		
		float bgx = (ww - bgw * numTiles) * camera.getXOffset(dt) / (ww - vw);
		float bgy = (wh - bgh) * camera.getYOffset(dt) / (wh - vh);
		
		int y = CameraUtil.getPixelY(bgy, camera, dt);
		int h = CameraUtil.getScaledSize(bgh, camera, dt);
		
		for (int i = 0; i < numTiles; i++) {
			int x0 = CameraUtil.getPixelX(bgx + bgw * i, camera, dt);
			int x1 = CameraUtil.getPixelX(bgx + bgw * (i + 1), camera, dt);

			if (x1 > 0 && x0 < renderer.getWidth())
				backgroundLayer.render(renderer, x0, y, x1 - x0, h);
		}
	}
}

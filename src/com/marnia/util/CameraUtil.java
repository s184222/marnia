package com.marnia.util;

import com.g4mesoft.camera.DynamicCamera;
import com.marnia.world.MarniaWorld;

public final class CameraUtil {

	private CameraUtil() {
	}
	
	public static int getPixelX(float xt, DynamicCamera camera, float dt) {
		return (int)((xt - camera.getXOffset(dt)) * camera.getScale(dt) * MarniaWorld.TILE_SIZE);
	}

	public static int getPixelY(float yt, DynamicCamera camera, float dt) {
		return (int)((yt - camera.getYOffset(dt)) * camera.getScale(dt) * MarniaWorld.TILE_SIZE);
	}
	
	public static int getScaledSize(float size, DynamicCamera camera, float dt) {
		return (int)(size * camera.getScale(dt) * MarniaWorld.TILE_SIZE);
	}
}

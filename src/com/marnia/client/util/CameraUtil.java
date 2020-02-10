package com.marnia.client.util;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.math.MathUtils;

public final class CameraUtil {

	private CameraUtil() {
	}
	
	public static int getPixelX(float xt, DynamicCamera camera, float dt) {
		return MathUtils.round((xt - camera.getXOffset(dt)) * camera.getScale(dt) + MathUtils.EPSILON);
	}

	public static int getPixelY(float yt, DynamicCamera camera, float dt) {
		return MathUtils.round((yt - camera.getYOffset(dt)) * camera.getScale(dt) + MathUtils.EPSILON);
	}
	
	public static int getScaledSize(float size, DynamicCamera camera, float dt) {
		return MathUtils.round(size * camera.getScale(dt) + MathUtils.EPSILON);
	}

	public static boolean isRectInBounds(float xc, float yc, float size, DynamicCamera camera, float dt) {
		int xp = getPixelX(xc, camera, dt);
		int yp = getPixelY(yc, camera, dt);
		int s = getScaledSize(size, camera, dt) / 2;
		return xp + s >= 0 && xp - s < camera.getScreenWidth() &&
		       yp + s >= 0 && yp - s < camera.getScreenHeight();
	}
}

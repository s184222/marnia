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
}

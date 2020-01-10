package com.marnia.client.world.entity;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.GColor;
import com.g4mesoft.graphic.IRenderer2D;
import com.marnia.client.util.CameraUtil;
import com.marnia.entity.Entity;
import com.marnia.entity.IController;
import com.marnia.world.MarniaWorld;

public class ClientPlayer extends Entity {

	public ClientPlayer(MarniaWorld world, IController controller) {
		super(world, controller);
	}
	
	public void render(IRenderer2D renderer, float dt, DynamicCamera camera) {
		float ix = prevPos.x + (pos.x - prevPos.x) * dt;
		float iy = prevPos.y + (pos.y - prevPos.y) * dt;
		
		int xp = CameraUtil.getPixelX(ix, camera, dt);
		int yp = CameraUtil.getPixelY(iy, camera, dt);
		int w = CameraUtil.getPixelX(ix + hitbox.x1 - hitbox.x0, camera, dt) - xp;
		int h = CameraUtil.getPixelY(iy + hitbox.y1 - hitbox.y0, camera, dt) - yp;
		
		renderer.setColor(GColor.HOT_PINK);
		renderer.fillRect(xp, yp, w, h);
	}
}

package com.marnia.world.player;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.GColor;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.math.Vec2f;
import com.marnia.util.CameraUtil;
import com.marnia.world.MarniaWorld;

public class Player {
	
	private final MarniaWorld world;
	private final IController controller;
	
	private Vec2f pos;
	private Vec2f prePos;
	
	public Player(MarniaWorld world, IController controller) {
		this.world = world;
		this.controller = controller;
	
		pos = new Vec2f();
		prePos = new Vec2f();
	}
	
	public void tick() {
		prePos.set(pos);
		controller.updatePos(pos);
	}
	
	public void render(IRenderer2D renderer, float dt, DynamicCamera camera) {
		float ix = prePos.x + (pos.x - prePos.x) * dt;
		float iy = prePos.y + (pos.y - prePos.y) * dt;
		int xp = CameraUtil.getPixelX(ix, camera, dt);
		int yp = CameraUtil.getPixelY(iy, camera, dt);
		int size = CameraUtil.getScaledSize(1, camera, dt);		
		
		renderer.setColor(GColor.HOT_PINK);
		renderer.fillRect(xp, yp, size, size);
	}
}

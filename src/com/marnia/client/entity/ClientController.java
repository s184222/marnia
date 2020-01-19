package com.marnia.client.entity;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.input.key.KeyInput;
import com.marnia.client.ClientMarniaApp;
import com.marnia.client.net.ClientGameplayNetworkManager;
import com.marnia.client.world.ClientMarniaWorld;
import com.marnia.entity.BasicController;
import com.marnia.entity.Entity;
import com.marnia.server.net.packet.S02PlayerPositionPacket;

public class ClientController extends BasicController {

	private static final int MAX_JUMP_TIME = 8;

	private static final float FRICTION_IDLE_X = 0.6f;
	private static final float IN_WATER_CAMERA_SCALE = 2.0f;

	private final KeyInput left;
	private final KeyInput right;
	private final KeyInput jump;

	private boolean canDoubleJump;
	private int jumpTimer;

	private boolean wasInWater;
	
	public ClientController(KeyInput left, KeyInput right, KeyInput jump) {
		this.left = left;
		this.right = right;
		this.jump = jump;
	}
	
	@Override
	public void update(Entity entity) {
		boolean moving = false;
		if (left.isPressed()) {
			entity.vel.x -= 0.15f;
			moving = true;
		}
		
		if (right.isPressed()) {
			entity.vel.x += 0.15f;
			moving = true;
		}

		if (entity.isOnGround())
			canDoubleJump = true;

		if(jump.isClicked() && canDoubleJump) {
			jumpTimer = entity.isOnGround() ? MAX_JUMP_TIME : 1;
			canDoubleJump = entity.isOnGround();
			entity.vel.y = -1.25f;
		} else if (!jump.isPressed() || entity.hitVerticalHitbox()) {
			jumpTimer = 0;
		}

		boolean inWater = entity.isInWater();
		if (jumpTimer > 0) {
			jumpTimer--;
		} else {
			applyGravity(entity);
		}
		
		applyFriction(entity, (moving || !entity.isOnGround()) ? FRICTION_X : FRICTION_IDLE_X, FRICTION_Y);
		
		entity.move();
		
		ClientMarniaApp app = ((ClientMarniaWorld)entity.world).getMarniaApp();

		if (inWater != wasInWater)
			app.setCameraScaleFactorTarget(inWater ? IN_WATER_CAMERA_SCALE : 1.0f);
		wasInWater = inWater;

		DynamicCamera camera = app.getCamera();
        camera.setCenterX((camera.getCenterX() + entity.getCenterX()) * 0.5f);
        camera.setCenterY((camera.getCenterY() + entity.getCenterY()) * 0.5f);

        ClientGameplayNetworkManager networkManager = app.getNetworkManager();
        networkManager.sendPacket(new S02PlayerPositionPacket(entity));
	}
}

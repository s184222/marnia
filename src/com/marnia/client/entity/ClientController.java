package com.marnia.client.entity;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.input.key.KeyInput;
import com.marnia.client.ClientMarniaApp;
import com.marnia.client.net.ClientGameplayNetworkManager;
import com.marnia.client.world.ClientMarniaWorld;
import com.marnia.entity.Entity;
import com.marnia.entity.IController;
import com.marnia.server.net.packet.S02PlayerPositionPacket;

public class ClientController implements IController {

	private static final int MAX_JUMP_TIME = 8;

	private final KeyInput left;
	private final KeyInput right;
	private final KeyInput jump;

	private boolean canDoubleJump;
	private int jumpTimer;
	
	public ClientController(KeyInput l, KeyInput r, KeyInput j) {
		left = l;
		right = r;
		jump = j;
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

		if (jumpTimer > 0) {
			jumpTimer--;
		} else {
			entity.vel.y += 0.25f;
		}

		entity.vel.mul(moving || !entity.isOnGround() ? 0.8f : 0.6f, 0.8f);
		entity.move();
		
		ClientMarniaApp app = ((ClientMarniaWorld)entity.world).getMarniaApp();
		
		DynamicCamera camera = app.getCamera();
        camera.setCenterX((camera.getCenterX() + entity.getCenterX()) * 0.5f);
        camera.setCenterY((camera.getCenterY() + entity.getCenterY()) * 0.5f);

        ClientGameplayNetworkManager networkManager = app.getNetworkManager();
        networkManager.sendPacket(new S02PlayerPositionPacket(entity.pos.x, entity.pos.y));
	}
}

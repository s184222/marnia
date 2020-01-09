package com.marnia.world.player;

import com.g4mesoft.input.key.KeyInput;

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
	public void update(Player player) {
		if (left.isPressed()) {
			player.vel.x -= 0.15f;
		}
		if (right.isPressed()) {
			player.vel.x += 0.15f;
		}

		if (player.isOnGround())
			canDoubleJump = true;

		if(jump.isClicked() && canDoubleJump) {
			jumpTimer = player.isOnGround() ? MAX_JUMP_TIME : 1;
			canDoubleJump = player.isOnGround();
			player.vel.y = -1.25f;
		} else if (!jump.isPressed() || player.hitVerticalHitbox()) {
			jumpTimer = 0;
		}

		if (jumpTimer > 0) {
			jumpTimer--;
		} else {
			player.vel.y += 0.25f;
		}


		player.vel.mul(0.8f);
		player.move();
	}
}

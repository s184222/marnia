package com.marnia.world.player;

import com.g4mesoft.input.key.KeyInput;

public class ClientController implements IController {

	private final KeyInput left;
	private final KeyInput right;
	private final KeyInput jump;

	private boolean canDoubleJump;
	
	public ClientController(KeyInput l, KeyInput r, KeyInput j) {
		left = l;
		right = r;
		jump = j;
	}
	
	@Override
	public void update(Player player) {
		if (left.isPressed()) {
			player.vel.x -= 0.1f;
		}
		if (right.isPressed()) {
			player.vel.x += 0.1f;
		}

		player.vel.y += 0.25f;

		if(jump.isClicked() && (player.isOnGround() || canDoubleJump)) {
			player.vel.y = -2.0f;
			canDoubleJump = player.isOnGround();
		}
		
		player.vel.mul(0.9f);
		player.move();
	}
}

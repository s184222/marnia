package com.marnia.world.player;

import com.g4mesoft.input.key.KeyInput;
import com.g4mesoft.math.Vec2f;

public class ClientController implements IController {

	private final KeyInput left;
	private final KeyInput right;
	private final KeyInput jump;
	
	public ClientController(KeyInput l, KeyInput r, KeyInput j) {
		left = l;
		right = r;
		jump = j;
	}
	
	@Override
	public void updatePos(Vec2f pos) {
		if (left.isPressed()) {
			pos.x -= 1.f;
		}
		if (right.isPressed()) {
			pos.x += 1.f;
		}
		//Jump
	}
	
	
	

}

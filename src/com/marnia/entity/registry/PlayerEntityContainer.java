package com.marnia.entity.registry;

import java.util.UUID;

import com.marnia.entity.PlayerColor;

public class PlayerEntityContainer extends EntityContainer {

	private final PlayerColor color;
	
	public PlayerEntityContainer(float x, float y, UUID identifier, PlayerColor color) {
		super(x, y, identifier);
		
		this.color = color;
	}
	
	public PlayerColor getColor() {
		return color;
	}
}

package com.marnia.server;

import java.util.UUID;

import com.marnia.entity.PlayerColor;

public class GameplayProfile {

	private final String name;
	private final UUID identifier;
	private final PlayerColor color;
	
	public GameplayProfile(String name, UUID identifier, PlayerColor color) {
		this.name = name;
		this.identifier = identifier;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	
	public UUID getIdentifier() {
		return identifier;
	}
	
	public PlayerColor getColor() {
		return color;
	}
}

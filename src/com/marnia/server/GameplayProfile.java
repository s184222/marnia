package com.marnia.server;

import java.util.UUID;

public class GameplayProfile {

	private final String name;
	private final UUID identifier;
	
	public GameplayProfile(String name, UUID identifier) {
		this.name = name;
		this.identifier = identifier;
	}
	
	public String getName() {
		return name;
	}
	
	public UUID getIdentifier() {
		return identifier;
	}
}

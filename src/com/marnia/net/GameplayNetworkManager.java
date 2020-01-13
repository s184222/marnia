package com.marnia.net;

import org.jspace.SequentialSpace;
import org.jspace.Space;

public class GameplayNetworkManager {

	private final Space publicGameplaySpace;
	private final String identifier;

	private final Space localGameplaySpace;
	
	public GameplayNetworkManager(Space publicGameplaySpace, String identifier) {
		this.publicGameplaySpace = publicGameplaySpace;
		this.identifier = identifier;

		localGameplaySpace = new SequentialSpace();
	}

	
}

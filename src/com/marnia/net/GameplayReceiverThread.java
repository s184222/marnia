package com.marnia.net;

import java.util.UUID;

public class GameplayReceiverThread extends Thread {

	private final GameplayNetworkManager networkManager;
	private final UUID identifier;
	
	public GameplayReceiverThread(GameplayNetworkManager networkManager, UUID identifier) {
		this.networkManager = networkManager;
		this.identifier = identifier;
	}
	
	@Override
	public void run() {
		
	}
}

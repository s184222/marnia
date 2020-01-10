package com.marnia;

import org.jspace.SequentialSpace;
import org.jspace.Space;

import com.g4mesoft.Application;
import com.g4mesoft.graphic.DisplayConfig;

public abstract class MarniaApp extends Application {

	protected static final String LOBBY_SPACE_NAME = "lobby";
	protected static final String GAMEPLAY_SPACE_NAME = "gameplay";
	
	protected Space lobbySpace;
	protected Space gameplaySpace;
	protected Space localSpace;
	
	public MarniaApp(DisplayConfig config) {
		super(config);
	}
	
	@Override
	public void init() {
		super.init();

		localSpace = new SequentialSpace();
		
		setDebug(false);
	}

	public String getGateAddress(String address, String port) {
		return getGateAddress(address, port, "");
	}
	
	public String getGateAddress(String address, String port, String spaceName) {
		return "tcp://" + address + ":" + port + "/" + spaceName + "?keep";
	}
	
	public Space getGameplaySpace() {
		return gameplaySpace;
	}

	public Space getLobbySpace() {
		return lobbySpace;
	}
}

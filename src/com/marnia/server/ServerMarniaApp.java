package com.marnia.server;

import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

import com.g4mesoft.Application;
import com.g4mesoft.graphic.DisplayConfig;
import com.g4mesoft.graphic.IRenderer2D;
import com.marnia.MarniaApp;
import com.marnia.server.world.ServerMarniaWorld;
import com.marnia.world.MarniaWorld;

public class ServerMarniaApp extends MarniaApp {

	private static final String ADDRESS = "192.0.0.1";
	private static final String PORT = "42069";
	
	private SpaceRepository spaceRepo;
	
	public ServerMarniaApp() {
		super(DisplayConfig.INVISIBLE_DISPLAY_CONFIG);
	}

	@Override
	public void init() {
		super.init();
		
		lobbySpace = new SequentialSpace();
		gameplaySpace = new SequentialSpace();
		
		spaceRepo = new SpaceRepository();
		spaceRepo.add(LOBBY_SPACE_NAME, lobbySpace);
		spaceRepo.add(GAMEPLAY_SPACE_NAME, gameplaySpace);
		spaceRepo.addGate(getGateAddress(ADDRESS, PORT));
	}
	
	@Override
	protected MarniaWorld initWorlds() {
		return new ServerMarniaWorld();
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	protected void render(IRenderer2D renderer, float dt) {
		// Do nothing on server (not called here).
	}
	
	public static void main(String[] args) throws Exception {
		Application.start(args, ServerMarniaApp.class);
	}
}

package com.marnia.server;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

import com.g4mesoft.Application;
import com.g4mesoft.graphic.DisplayConfig;
import com.g4mesoft.graphic.IRenderer2D;
import com.marnia.MarniaApp;
import com.marnia.net.ILobbyEventListener;
import com.marnia.net.LobbyArea;
import com.marnia.server.net.ServerGameplayNetworkManager;
import com.marnia.server.net.ServerLobbyArea;

public class ServerMarniaApp extends MarniaApp implements ILobbyEventListener {

	private static final String ADDRESS = "192.168.1.12";
	private static final String PORT = "42069";
	
	private static final long MAX_WAIT_TIME = 30L * 1000L;
	
	private final UUID serverIdentifier;
	private final List<GameplaySession> sessions;
	
	private SpaceRepository spaceRepo;
	private ServerLobbyArea lobbyArea;
	
	private ServerGameplayNetworkManager networkManager;
	
	private long lastPlayerJoinTime;
	private boolean shouldAttemptStartGame;
	
	
	public ServerMarniaApp() {
		super(DisplayConfig.INVISIBLE_DISPLAY_CONFIG);
	
		serverIdentifier = UUID.randomUUID();
		sessions = new ArrayList<GameplaySession>();
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
	
		lobbyArea = new ServerLobbyArea(this, serverIdentifier, lobbySpace);
		lobbyArea.start();
		
		networkManager = new ServerGameplayNetworkManager(gameplaySpace, serverIdentifier, registry);
		networkManager.start();
	}
	
	@Override
	public void stop() {
		lobbyArea.stop();
		networkManager.stop();
		
		super.stop();
	}
	
	@Override
	public void tick() {
		if (shouldAttemptStartGame) {
			long deltaTime = System.currentTimeMillis() - lastPlayerJoinTime;
			if (deltaTime >= MAX_WAIT_TIME) {
				shouldAttemptStartGame = false;
				
				GameplaySession session = lobbyArea.startGame();
				if (session.startGame())
					sessions.add(session);
			}
		}
		
		for (GameplaySession session : sessions)
			session.tick();

		networkManager.tick();
	}
	
	@Override
	protected void render(IRenderer2D renderer, float dt) {
		// Do nothing on server (not called here).
	}

	@Override
	public void onLobbyEvent(int eventId) {
		if (eventId == LobbyArea.PLAYER_ADDED_EVENT) {
			lastPlayerJoinTime = System.currentTimeMillis();
			shouldAttemptStartGame = true;
		}
	}
	
	public UUID getUniqueIdentifier() {
		// // TODO: For now just a random uuid, but fix later.
		return UUID.randomUUID();
	}

	public ServerGameplayNetworkManager getNetworkManager() {
		return networkManager;
	}

	public static void main(String[] args) throws Exception {
		Application.start(args, ServerMarniaApp.class);
	}
}

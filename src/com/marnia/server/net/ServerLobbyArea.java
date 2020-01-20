package com.marnia.server.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.jspace.Space;

import com.marnia.entity.PlayerColor;
import com.marnia.net.LobbyArea;
import com.marnia.net.LobbyThread;
import com.marnia.server.GameplayProfile;
import com.marnia.server.GameplaySession;
import com.marnia.server.ServerMarniaApp;

public class ServerLobbyArea extends LobbyArea {

	private static final int MIN_NUM_PLAYERS = 1;
	
	private final ServerMarniaApp app;
	private final UUID serverIdentifier;
	
	public ServerLobbyArea(ServerMarniaApp app, UUID serverIdentifier, Space publicLobbySpace) {
		super(publicLobbySpace);
		
		this.app = app;
		this.serverIdentifier = serverIdentifier;
	}
	
	public GameplaySession startGame() {
		List<String> players = getPlayers();
		if (players.size() < MIN_NUM_PLAYERS)
			return null;
		
		List<GameplayProfile> profiles = new ArrayList<GameplayProfile>();
		
		List<PlayerColor> colors = new ArrayList<PlayerColor>();
		for (PlayerColor color : PlayerColor.values())
			colors.add(color);
		Collections.shuffle(colors);
		
		UUID[] identifiers = app.createUniqueIdentifiers(players.size());
		for (int i = 0; i < players.size(); i++) {
			PlayerColor color = colors.get(i % colors.size());
			profiles.add(new GameplayProfile(players.get(i), identifiers[i], color));
		}
		
		removePlayers(players);
		
		sendGameStartEvent(profiles);
		
		return new GameplaySession(app.getNetworkManager(), profiles);
	}
	
	private void sendGameStartEvent(List<GameplayProfile> profiles) {
		for (GameplayProfile profile : profiles) {
			try {
				publicLobbySpace.put(LobbyArea.SERVER_RESPONSE, profile.getName(), serverIdentifier, profile.getIdentifier());
				publicLobbySpace.put(LobbyArea.SERVER_RESPONSE, profile.getName(), START_GAME_TYPE);
			} catch (InterruptedException e) {
			}
		}
	}
	
	@Override
	protected LobbyThread createLobbyThread() {
		return new ServerLobbyThread(publicLobbySpace, localLobbySpace);
	}
}

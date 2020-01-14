package com.marnia.server.net;

import java.util.ArrayList;
import java.util.List;

import org.jspace.Space;

import com.marnia.client.net.ClientLobbyArea;
import com.marnia.net.LobbyArea;
import com.marnia.net.LobbyThread;
import com.marnia.util.SpaceHelper;

public class ServerLobbyThread extends LobbyThread {

	public ServerLobbyThread(Space publicLobbySpace, Space localLobbySpace) {
		super(publicLobbySpace, localLobbySpace);
	}

	private String getClientRequest() throws InterruptedException {
		Object[] connectInfo = publicLobbySpace.get(LobbyArea.CLIENT_REQUEST_MATCH, 
				SpaceHelper.STRING_MATCH);
		return (String)connectInfo[1];
	}
	
	private <T> void sendResponseToPlayer(String username, T data) throws InterruptedException {
		publicLobbySpace.put(LobbyArea.SERVER_RESPONSE, username, data);
	}

	private List<String> getAllPlayers() throws InterruptedException {
		List<Object[]> playerInfos = localLobbySpace.queryAll(LobbyArea.LOCAL_PLAYER_MATCH, SpaceHelper.STRING_MATCH);
		
		List<String> playerNames = new ArrayList<String>();
		for (Object[] playerInfo : playerInfos)
			playerNames.add((String)playerInfo[1]);
		return playerNames;
	}
	
	private void listenForPlayers() {
		while (true) {
			try {
				if (!isLobbyRunning())
					break;
				
				String username = getClientRequest();
				
				if (isPlayerInLobby(username)) {
					sendResponseToPlayer(username, LobbyArea.CONNECTION_FAILED_TYPE);
				} else {
					List<String> playerNames = getAllPlayers();
					for (String playerName : playerNames) {
						sendResponseToPlayer(playerName, username);
						sendResponseToPlayer(playerName, LobbyArea.PLAYER_JOINED_TYPE);
					}
					
					addPlayerToLobby(username);
					sendResponseToPlayer(username, LobbyArea.CONNECTION_SUCCESSFUL_TYPE);
					
					for(String playerName : playerNames) {
						sendResponseToPlayer(username, playerName);
						sendResponseToPlayer(username, LobbyArea.PLAYER_JOINED_TYPE);
					}
				}
			} catch (InterruptedException e) {
			}
		}
	}
	
	@Override
	public void run() {
		listenForPlayers();
		
		try {
			dispatchLobbyEvent(ClientLobbyArea.LOBBY_CLOSED_EVENT);
		} catch (InterruptedException e) {
		}
	}
}

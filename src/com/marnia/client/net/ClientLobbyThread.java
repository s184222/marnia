package com.marnia.client.net;

import java.util.UUID;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Space;

import com.marnia.net.LobbyArea;
import com.marnia.net.LobbyThread;
import com.marnia.util.SpaceHelper;

public class ClientLobbyThread extends LobbyThread {

	private final String username;
	private final ActualField usernameMatch;
	
	public ClientLobbyThread(Space publicLobbySpace, Space localLobbySpace, String username) {
		super(publicLobbySpace, localLobbySpace);

		this.username = username;
		this.usernameMatch = new ActualField(username);
	}

	@SuppressWarnings("unchecked")
	private <T> T getServerResponse(Class<T> clazz) throws InterruptedException {
		Object[] resp = publicLobbySpace.get(LobbyArea.SERVER_RESPONSE_MATCH, 
				usernameMatch, new FormalField(clazz));
		// Response is null in the case where the server closes.
		return (resp == null) ? null : (T)resp[2];
	}
	
	private int getServerEvent() throws InterruptedException {
		Integer eventId = getServerResponse(Integer.class);
		return (eventId == null) ? -1 : eventId.intValue();
	}
	
	private boolean connectToLobby() throws InterruptedException {
		publicLobbySpace.put(LobbyArea.CLIENT_REQUEST, username);
		return getServerEvent() == LobbyArea.CONNECTION_SUCCESSFUL_TYPE;
	}
	
	@Override
	public void run() {
		try {
			if (connectToLobby()) {
				dispatchLobbyEvent(ClientLobbyArea.CONNECTION_SUCCESSFUL_EVENT);
				
				// Add our own username to the lobby.
				addPlayerToLobby(username);
				
				int serverEvent;
				while((serverEvent = getServerEvent()) != -1) {
					if (serverEvent == LobbyArea.PLAYER_JOINED_TYPE) {
						String playerName = getServerResponse(String.class);
						// Server closed
						if (playerName == null)
							break;
						addPlayerToLobby(playerName);
					} else if (serverEvent == LobbyArea.START_GAME_TYPE) {
						Object[] resp = publicLobbySpace.get(LobbyArea.SERVER_RESPONSE_MATCH, 
								usernameMatch, SpaceHelper.UUID_MATCH, SpaceHelper.UUID_MATCH);
						
						UUID serverIdentifier = (UUID)resp[2];
						UUID clientIdentifier = (UUID)resp[3];

						localLobbySpace.put(ClientLobbyArea.LOCAL_SERVER_IDENTIFIER_FIELD, serverIdentifier);
						localLobbySpace.put(ClientLobbyArea.LOCAL_CLIENT_IDENTIFIER_FIELD, clientIdentifier);
					
						dispatchLobbyEvent(LobbyArea.GAME_STARTING_EVENT);
						
						// Close thread when game starts.
						break;
					} else {
						System.out.println("Received invalid event from server: " + serverEvent);
					}
				}
			}
		} catch (InterruptedException e) {
		}
		
		try {
			dispatchLobbyEvent(ClientLobbyArea.LOBBY_CLOSED_EVENT);
		} catch (InterruptedException e) {
		}
	}
}

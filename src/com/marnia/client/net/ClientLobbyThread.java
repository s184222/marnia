package com.marnia.client.net;

import org.jspace.ActualField;
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

	private int getServerEvent() throws InterruptedException {
		Object[] resp = publicLobbySpace.get(LobbyArea.SERVER_RESPONSE_MATCH, 
				usernameMatch, SpaceHelper.INTEGER_MATCH);
		// Response is null in the case where the server closes.
		return (resp == null) ? -1 : (Integer)resp[2];
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
						Object[] newPlayerInfo = publicLobbySpace.get(LobbyArea.SERVER_RESPONSE_MATCH,
								usernameMatch, SpaceHelper.STRING_MATCH);
						
						// Server closed
						if (newPlayerInfo == null)
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

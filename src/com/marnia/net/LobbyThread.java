package com.marnia.net;

import org.jspace.ActualField;
import org.jspace.Space;

import com.marnia.client.net.ClientLobbyArea;

public abstract class LobbyThread extends Thread {

	protected final Space publicLobbySpace;
	protected final Space localLobbySpace;
	
	public LobbyThread(Space publicLobbySpace, Space localLobbySpace) {
		super("Lobby thread");
		
		this.publicLobbySpace = publicLobbySpace;
		this.localLobbySpace = localLobbySpace;

		setDaemon(true);
	}

	protected void dispatchLobbyEvent(int event) throws InterruptedException {
		localLobbySpace.put(event);
	}
	
	protected boolean isPlayerInLobby(String username) throws InterruptedException {
		return localLobbySpace.queryp(LobbyArea.LOCAL_PLAYER_MATCH, new ActualField(username)) != null;
	}

	protected void addPlayerToLobby(String username) throws InterruptedException {
		localLobbySpace.put(LobbyArea.LOCAL_PLAYER_FIELD, username);
		dispatchLobbyEvent(ClientLobbyArea.PLAYER_ADDED_EVENT);
	}

	protected void removePlayerFromLobby(String username) throws InterruptedException {
		localLobbySpace.getp(LobbyArea.LOCAL_PLAYER_MATCH, new ActualField(username));
		dispatchLobbyEvent(ClientLobbyArea.PLAYER_ADDED_EVENT);
	}
	
	protected boolean isLobbyRunning() throws InterruptedException {
		return localLobbySpace.queryp(LobbyArea.LOCAL_STATUS_MATCH, LobbyArea.RUNNING_MATCH) != null;
	}
	
	@Override
	public abstract void run();
	
}

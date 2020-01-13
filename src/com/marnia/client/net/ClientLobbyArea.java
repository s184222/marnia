package com.marnia.client.net;

import org.jspace.Space;

import com.marnia.net.LobbyArea;
import com.marnia.net.LobbyThread;

public class ClientLobbyArea extends LobbyArea {
	
	public static final int CONNECTION_SUCCESSFUL_EVENT = 2;
	
	private String username;
	
	public ClientLobbyArea(Space publicLobbySpace){
		super(publicLobbySpace);
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	protected LobbyThread createLobbyThread() {
		if (username == null)
			throw new IllegalStateException("Username has not been set!");
		return new ClientLobbyThread(publicLobbySpace, localLobbySpace, username);
	}
	
	@Override
	public void stop() {
		super.stop();
		
		username = null;
	}
}

package com.marnia.client.net;

import java.util.UUID;

import org.jspace.ActualField;
import org.jspace.Space;

import com.marnia.net.LobbyArea;
import com.marnia.net.LobbyThread;

public class ClientLobbyArea extends LobbyArea {
	
	public static final int CONNECTION_SUCCESSFUL_EVENT = 16;
	
	public static final int LOCAL_SERVER_IDENTIFIER_FIELD = 16;
	public static final int LOCAL_CLIENT_IDENTIFIER_FIELD = 17;

	public static final ActualField LOCAL_SERVER_IDENTIFIER_MATCH = new ActualField(LOCAL_SERVER_IDENTIFIER_FIELD);
	public static final ActualField LOCAL_CLIENT_IDENTIFIER_MATCH = new ActualField(LOCAL_CLIENT_IDENTIFIER_FIELD);
	
	private String username;
	
	public ClientLobbyArea(Space publicLobbySpace){
		super(publicLobbySpace);
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public UUID getServerIdentifier() {
		return queryLocalField(LOCAL_SERVER_IDENTIFIER_MATCH, UUID.class);
	}

	public UUID getClientIdentifier() {
		return queryLocalField(LOCAL_CLIENT_IDENTIFIER_MATCH, UUID.class);
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

package com.marnia.server.net;

import org.jspace.Space;

import com.marnia.net.LobbyArea;
import com.marnia.net.LobbyThread;

public class ServerLobbyArea extends LobbyArea {

	public ServerLobbyArea(Space publicLobbySpace) {
		super(publicLobbySpace);
	}
	
	@Override
	protected LobbyThread createLobbyThread() {
		return new ServerLobbyThread(publicLobbySpace, localLobbySpace);
	}
}

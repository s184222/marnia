package com.marnia.server.net;

import java.util.List;

import org.jspace.ActualField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import com.marnia.util.SpaceHelper;
import static com.marnia.util.LobbyHelper.*;

public class ServerLobbyArea {


	private final Space publicLobbySpace;
	private final Space localLobbySpace;
	
	private final ServerLobbyThread lobbyThread;
	
	public ServerLobbyArea(Space publicLobbySpace) {
		this.publicLobbySpace = publicLobbySpace;
		localLobbySpace = new SequentialSpace();
		
		lobbyThread = new ServerLobbyThread();
	}
	
	public void start() {
		try {
			localLobbySpace.put(LOCAL_STATUS_FIELD, RUNNING_FIELD);
		} catch (InterruptedException e) {
		}
		
		lobbyThread.start();
	}
	
	public void stop() {
		try {
			localLobbySpace.getp(LOCAL_STATUS_MATCH, RUNNING_MATCH);
			lobbyThread.interrupt();
			lobbyThread.join();
		} catch (InterruptedException e) {
		}
	}
	
	private class ServerLobbyThread extends Thread {
		
		public ServerLobbyThread() {
			super("Lobby thread");
			
			setDaemon(true);
		}
		
		@Override
		public void run() {
			boolean running = true;
			
			while (running) {
				try {
					if (localLobbySpace.queryp(LOCAL_STATUS_MATCH, RUNNING_MATCH) != null) {
						Object[] connectInfo = publicLobbySpace.get(CLIENT_REQUEST_MATCH, SpaceHelper.STRING_MATCH);
						
						String username = (String)connectInfo[1];
						
						if (localLobbySpace.queryp(LOCAL_PLAYER_MATCH, new ActualField(username)) != null) {
							publicLobbySpace.put(SERVER_RESPONSE, username, CONNECTION_FAILED_TYPE);
						} else {
							List<Object[]> playerInfos = localLobbySpace.queryAll(LOCAL_PLAYER_MATCH, SpaceHelper.STRING_MATCH);
							for(Object[] playerInfo : playerInfos) {
								publicLobbySpace.put(SERVER_RESPONSE, playerInfo[1], username);
								publicLobbySpace.put(SERVER_RESPONSE, playerInfo[1], PLAYER_JOINED_TYPE);
							}
							
							localLobbySpace.put(LOCAL_PLAYER_FIELD, username);
							publicLobbySpace.put(SERVER_RESPONSE, username, CONNECTION_SUCCESSFUL_TYPE);
							
							for(Object[] playerInfo : playerInfos) {
								publicLobbySpace.put(SERVER_RESPONSE, username, playerInfo[1]);
								publicLobbySpace.put(SERVER_RESPONSE, username, PLAYER_JOINED_TYPE);
							}
						}
					} else {
						running = false;						
					}					
				} catch (InterruptedException e) {
				}
			}
		}
	}
}

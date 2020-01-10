package com.marnia.client.net;

import static com.marnia.util.LobbyHelper.*;

import java.util.ArrayList;
import java.util.List;

import org.jspace.ActualField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import com.marnia.util.SpaceHelper;

public class ClientLobbyArea {
	
	private static final int LOBBY_CLOSED_EVENT = 0;
	private static final int CONNECTION_SUCCESSFUL_EVENT = 1;
	private static final int NEW_PLAYER_EVENT = 2;
	
	private final Space publicLobbySpace;
	private final Space localLobbySpace;
	
	private final List<ILobbyEventListener> listeners;
	
	private ClientLobbyThread connectionThread;
	
	public ClientLobbyArea(Space publicLobbySpace){
		this.publicLobbySpace = publicLobbySpace;
		localLobbySpace = new SequentialSpace();
		listeners = new ArrayList<ILobbyEventListener>();

		try {
			localLobbySpace.put(SpaceHelper.LOCK);
		} catch (InterruptedException e) {
		}
	}
	
	public void start(String username) {
		if (connectionThread != null)
			throw new IllegalStateException("Already connecting!");
		
		connectionThread = new ClientLobbyThread(username);
		
		try {
			localLobbySpace.put(LOCAL_STATUS_FIELD, RUNNING_FIELD);
		} catch (InterruptedException e) {
		}
		connectionThread.start();
	}
	
	public void stop() {
		if (connectionThread != null) {
			try {
				localLobbySpace.getp(LOCAL_STATUS_MATCH, RUNNING_MATCH);
				connectionThread.interrupt();
				connectionThread.join();
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void tick() {
		try {
			localLobbySpace.get(SpaceHelper.LOCK_MATCH);
			List<Object[]> events = localLobbySpace.getAll(SpaceHelper.INTEGER_MATCH);
			for (Object[] event : events)
				dispatchEvent((Integer)event[0]);
			localLobbySpace.put(SpaceHelper.LOCK);
		} catch (InterruptedException e) {
		}
	}
	
	private void dispatchEvent(int eventId) {
		for (ILobbyEventListener listener : listeners) {
			switch (eventId) {
			case LOBBY_CLOSED_EVENT:
				listener.onLobbyClosed();
				break;
			case CONNECTION_SUCCESSFUL_EVENT:
				listener.onConnectionSuccessful();
				break;
			case NEW_PLAYER_EVENT:
				listener.onNewPlayer();
				break;
			}
		}
	}
	
	public void addEventListener(ILobbyEventListener listener) {
		listeners.add(listener);
	}

	public void removeEventListener(ILobbyEventListener listener) {
		listeners.remove(listener);
	}
	
	public List<String> getPlayers() {
		List<String> result = new ArrayList<String>();
		
		List<Object[]> playerInfos = null;
		try {
			playerInfos = localLobbySpace.queryAll(LOCAL_PLAYER_MATCH, SpaceHelper.STRING_MATCH);
		} catch (InterruptedException e) {
		}
		
		if (playerInfos != null) {
			for (Object[] playerInfo : playerInfos)
				result.add((String)playerInfo[1]);
		}
		
		return result;
	}
	
	private class ClientLobbyThread extends Thread {
		
		private final String username;
		private final ActualField usernameMatch;
		
		public ClientLobbyThread(String username) {
			super("Lobby thread");
			
			setDaemon(true);
			
			this.username = username;
			usernameMatch = new ActualField(username);
		}
		
		private void dispatchLobbyEvent(int event) throws InterruptedException {
			localLobbySpace.get(SpaceHelper.LOCK_MATCH);
			localLobbySpace.put(event);
			localLobbySpace.put(SpaceHelper.LOCK);
		}
		
		@Override
		public void run() {
			try {
				publicLobbySpace.put(CLIENT_REQUEST, username);
				
				Object[] connectionInfo = publicLobbySpace.get(SERVER_RESPONSE_MATCH, 
						usernameMatch, SpaceHelper.INTEGER_MATCH);
				
				if (connectionInfo == null) {
					// Server closed.
					return;
				}
				
				if ((Integer)connectionInfo[2] == CONNECTION_SUCCESSFUL_TYPE) {
					dispatchLobbyEvent(CONNECTION_SUCCESSFUL_EVENT);

					localLobbySpace.put(LOCAL_PLAYER_FIELD, username);
					dispatchLobbyEvent(NEW_PLAYER_EVENT);
					
					while(true) {
						Object[] eventInfo = publicLobbySpace.get(SERVER_RESPONSE_MATCH, 
								usernameMatch, SpaceHelper.INTEGER_MATCH);
						
						if (eventInfo == null)
							return;
						
						switch ((Integer)eventInfo[2]) {
						case PLAYER_JOINED_TYPE:
							Object[] newPlayerInfo = publicLobbySpace.get(SERVER_RESPONSE_MATCH,
									usernameMatch, SpaceHelper.STRING_MATCH);
						
							if (newPlayerInfo == null)
								return;
							
							localLobbySpace.put(LOCAL_PLAYER_FIELD, newPlayerInfo[2]);

							dispatchLobbyEvent(NEW_PLAYER_EVENT);
							break;
	
						default:
							System.out.println("Received invalid packet from server: " + eventInfo[2]);
						}
					}
				}
			} catch (InterruptedException e) {
			}
			
			try {
				dispatchLobbyEvent(LOBBY_CLOSED_EVENT);
			} catch (InterruptedException e) {
			}
		}
	}
}

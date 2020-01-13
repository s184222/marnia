package com.marnia.net;

import java.util.ArrayList;
import java.util.List;

import org.jspace.ActualField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import com.marnia.util.SpaceHelper;

public abstract class LobbyArea {

	public static final int LOBBY_CLOSED_EVENT = 0;
	public static final int PLAYER_ADDED_EVENT = 1;
	
	public static final int CLIENT_REQUEST = 0;
	public static final int SERVER_RESPONSE = 1;

	public static final int LOCAL_STATUS_FIELD = 0;
	public static final int LOCAL_PLAYER_FIELD = 1;
	
	public static final int CONNECTION_FAILED_TYPE = 0;
	public static final int CONNECTION_SUCCESSFUL_TYPE = 1;
	public static final int PLAYER_JOINED_TYPE = 2;
	
	public static final String RUNNING_FIELD = "running";
	
	public static final ActualField CLIENT_REQUEST_MATCH = new ActualField(CLIENT_REQUEST);
	public static final ActualField SERVER_RESPONSE_MATCH = new ActualField(SERVER_RESPONSE);
	
	public static final ActualField LOCAL_STATUS_MATCH = new ActualField(LOCAL_STATUS_FIELD);
	public static final ActualField LOCAL_PLAYER_MATCH = new ActualField(LOCAL_PLAYER_FIELD);

	public static final ActualField CONNECTION_FAILED_MATCH = new ActualField(CONNECTION_FAILED_TYPE);
	public static final ActualField CONNECTION_SUCCESSFUL_MATCH = new ActualField(CONNECTION_SUCCESSFUL_TYPE);
	public static final ActualField PLAYER_JOINED_MATCH = new ActualField(PLAYER_JOINED_TYPE);
	
	public static final ActualField RUNNING_MATCH = new ActualField(RUNNING_FIELD);
	
	protected final Space publicLobbySpace;
	protected final Space localLobbySpace;

	private final List<ILobbyEventListener> listeners;
	
	private LobbyThread lobbyThread;
	
	public LobbyArea(Space publicLobbySpace) {
		this.publicLobbySpace = publicLobbySpace;
		localLobbySpace = new SequentialSpace();

		listeners = new ArrayList<ILobbyEventListener>();
		
		lobbyThread = null;
		
		try {
			localLobbySpace.put(SpaceHelper.LOCK);
		} catch (InterruptedException e) {
		}
	}
	
	public void start() {
		if (lobbyThread != null)
			throw new IllegalStateException("Lobby already started!");
		
		lobbyThread = createLobbyThread();
		
		try {
			localLobbySpace.put(LOCAL_STATUS_FIELD, RUNNING_FIELD);
		} catch (InterruptedException e) {
		}
		lobbyThread.start();
	}
	
	protected abstract LobbyThread createLobbyThread();
	
	public void stop() {
		if (lobbyThread != null) {
			try {
				localLobbySpace.getp(LOCAL_STATUS_MATCH, RUNNING_MATCH);
				
				lobbyThread.interrupt();
				lobbyThread.join();
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
		for (ILobbyEventListener listener : listeners)
			listener.onLobbyEvent(eventId);
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
}

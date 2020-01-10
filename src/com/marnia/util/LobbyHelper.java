package com.marnia.util;

import org.jspace.ActualField;

public class LobbyHelper {

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
	
}

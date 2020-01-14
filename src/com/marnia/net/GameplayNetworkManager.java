package com.marnia.net;

import org.jspace.ActualField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import com.marnia.net.packet.INetworkHandler;
import com.marnia.net.packet.IPacket;

public class GameplayNetworkManager<H extends INetworkHandler> {

	public static final int PACKET_TO_HANDLE = 0;
	public static final int PACKET_TO_SEND = 1;
	public static final int RUNTIME_FIELDS = 2;
	
	public static final ActualField PACKET_TO_HANDLE_MATCH = new ActualField(PACKET_TO_HANDLE);
	public static final ActualField PACKET_TO_SEND_MATCH = new ActualField(PACKET_TO_SEND);
	public static final ActualField RUNTIME_FIELDS_MATCH = new ActualField(RUNTIME_FIELDS);
	
	public static final int FIELD_RUNNING = 2;
	
	public static final ActualField FIELD_RUNNING_MATCH = new ActualField(FIELD_RUNNING);
	
	private final Space publicGameplaySpace;
	private final String identifier;

	private final Space localGameplaySpace;
	
	public GameplayNetworkManager(Space publicGameplaySpace, String identifier) {
		this.publicGameplaySpace = publicGameplaySpace;
		this.identifier = identifier;

		localGameplaySpace = new SequentialSpace();
	}

	public IPacket<H> getPacketByType(int packetType) {
		return null;
	}
}

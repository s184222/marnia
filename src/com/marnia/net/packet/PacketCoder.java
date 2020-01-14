package com.marnia.net.packet;

import java.util.UUID;

import org.jspace.Space;

public abstract class PacketCoder {

	protected static final int PACKET_OVERHEAD = 2;
	
	protected final UUID receiver;
	protected final UUID sender;
	protected final Space space;
	
	public PacketCoder(UUID receiver, UUID sender, Space space) {
		this.receiver = receiver;
		this.sender = sender;
		this.space = space;
	}
	
	public UUID getReceiver() {
		return receiver;
	}
	
	public UUID getSender() {
		return sender;
	}
	
	public Space getSpace() {
		return space;
	}
}

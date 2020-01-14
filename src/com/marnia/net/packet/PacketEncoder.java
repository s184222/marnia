package com.marnia.net.packet;

import java.util.UUID;

import org.jspace.Space;

public class PacketEncoder extends PacketCoder {

	public PacketEncoder(UUID receiver, UUID sender, Space space) {
		super(receiver, sender, space);
	}
	
	public void putData(Object... packetData) throws InterruptedException {
		Object[] data = new Object[PACKET_OVERHEAD + packetData.length];
		data[0] = receiver;
		data[1] = sender;
		System.arraycopy(packetData, 0, data, PACKET_OVERHEAD, packetData.length);
		space.put(data);
	}
}

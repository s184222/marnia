package com.marnia.net;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.marnia.net.packet.IPacket;

public class PacketRegistry {

	private final Map<Class<? extends IPacket<?>>, Integer> packetToId;
	private final Map<Integer, Class<? extends IPacket<?>>> idToPacket;

	public PacketRegistry() {
		packetToId = new ConcurrentHashMap<Class<? extends IPacket<?>>, Integer>();
		idToPacket = new ConcurrentHashMap<Integer, Class<? extends IPacket<?>>>();
	}
	
	public void addPacketType(Class<? extends IPacket<?>> packetClazz, int id) {
		if (packetToId.containsKey(packetClazz))
			throw new IllegalArgumentException("Packet already registered.");
		if (idToPacket.containsKey(id))
			throw new IllegalArgumentException("Id already registered.");

		packetToId.put(packetClazz, id);
		idToPacket.put(id, packetClazz);
	}

	public Class<? extends IPacket<?>> getPacketFromId(int id) {
		return idToPacket.get(id);
	}

	public int getIdFromPacket(Class<? extends IPacket<?>> packet) {
		return packetToId.get(packet);
	}
}

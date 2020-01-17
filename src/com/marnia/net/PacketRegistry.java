package com.marnia.net;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.marnia.net.packet.IPacket;

public class PacketRegistry {

	private final Map<Class<? extends IPacket<?>>, Integer> packetToId;
	private final Map<Integer, Class<? extends IPacket<?>>> idToPacket;
	private final Map<Integer, IPacketProvider<?>> idToProvider;

	public PacketRegistry() {
		packetToId = new ConcurrentHashMap<Class<? extends IPacket<?>>, Integer>();
		idToPacket = new ConcurrentHashMap<Integer, Class<? extends IPacket<?>>>();
		idToProvider = new ConcurrentHashMap<Integer, IPacketProvider<?>>();
	}
	
	public <P extends IPacket<?>> void addPacketType(Class<P> packetClazz, int id, IPacketProvider<P> provider) {
		if (packetToId.containsKey(packetClazz))
			throw new IllegalArgumentException("Packet already registered.");
		if (idToPacket.containsKey(id))
			throw new IllegalArgumentException("Id already registered.");

		packetToId.put(packetClazz, id);
		idToPacket.put(id, packetClazz);
		idToProvider.put(id, provider);
	}

	public IPacketProvider<?> getPacketProviderFromId(int id) {
		return idToProvider.get(id);
	}
	
	public Class<? extends IPacket<?>> getPacketFromId(int id) {
		return idToPacket.get(id);
	}

	public int getIdFromPacket(Class<? extends IPacket<?>> packet) {
		return packetToId.get(packet);
	}
}

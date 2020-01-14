package com.marnia.net;

import java.util.List;
import java.util.UUID;

import org.jspace.ActualField;
import org.jspace.Space;

import com.marnia.net.packet.INetworkHandler;
import com.marnia.net.packet.IPacket;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.util.SpaceHelper;

public class GameplayReceiverThread<H extends INetworkHandler> extends GameplayNetworkThread<H> {

	private final ActualField identifierMatch;
	
	public GameplayReceiverThread(GameplayNetworkManager<H> manager, Space publicSpace, Space localSpace, UUID identifier) {
		super(manager, publicSpace, localSpace, identifier, "Gameplay receiver thread");
	
		identifierMatch = new ActualField(identifier);
	}

	private void handlePacket(UUID senderIdentifier, int packetType) {
		IPacket<H> packet = manager.getPacketByType(packetType);
		if (packet != null) {
			try {
				packet.decodePacket(new PacketDecoder(identifier, senderIdentifier, publicSpace));
				localSpace.put(GameplayNetworkManager.PACKET_TO_HANDLE, senderIdentifier, packet);
			} catch (Exception e) {
				// Some error occurred whilst decoding...
			}
		}
	}
	
	@Override
	public boolean processPacket() throws InterruptedException {
		List<Object[]> packetsInfo = publicSpace.getAll(identifierMatch, SpaceHelper.UUID_MATCH,
				SpaceHelper.INTEGER_MATCH);

		if (packetsInfo == null)
			return false;
		
		for (Object[] packetTypeInfo : packetsInfo) {
			UUID senderIdentifier = (UUID)packetTypeInfo[1];
			int packetType = ((Integer)packetTypeInfo[2]).intValue();
			handlePacket(senderIdentifier, packetType);
		}
		
		return true;
	}
}

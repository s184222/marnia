package com.marnia.net;

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
		IPacket<?> packet = manager.getPacketByType(packetType);
		if (packet != null) {
			try {
				packet.decodePacket(new PacketDecoder(identifier, senderIdentifier, publicSpace));
				localSpace.put(GameplayNetworkManager.PACKET_TO_HANDLE, senderIdentifier, packet);
			} catch (Exception e) {
				// Some error occurred whilst decoding...
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean processPacket() throws InterruptedException {
		Object[] packetTypeInfo = publicSpace.get(identifierMatch, SpaceHelper.UUID_MATCH, SpaceHelper.INTEGER_MATCH);
		
		if (packetTypeInfo == null)
			return false;
		
		UUID senderIdentifier = (UUID)packetTypeInfo[1];
		int packetType = ((Integer)packetTypeInfo[2]).intValue();
		handlePacket(senderIdentifier, packetType);
		
		return true;
	}
}

package com.marnia.net;

import java.util.UUID;

import org.jspace.ActualField;
import org.jspace.Space;

import com.marnia.net.packet.INetworkHandler;
import com.marnia.net.packet.IPacket;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.util.SpaceHelper;

public class GameplayReceiverThread<H extends INetworkHandler> extends GameplayNetworkThread<H> {

	private final ActualField idMatch;
	
	public GameplayReceiverThread(GameplayNetworkManager<H> manager, Space publicSpace, Space localSpace, UUID identifier) {
		super(manager, publicSpace, localSpace, identifier, "Gameplay receiver thread");
		
		idMatch = new ActualField(identifier);
	}

	@Override
	public void run() {
		try {
			while (isGameplayRunning()) {
				Object[] packetTypeInfo = publicSpace.get(idMatch, SpaceHelper.UUID_MATCH, SpaceHelper.INTEGER_MATCH);
				
				UUID senderIdentifier = (UUID)packetTypeInfo[1];
				int packetType = ((Integer)packetTypeInfo[2]).intValue();
			
				IPacket<H> packet = manager.getPacketByType(packetType);
				packet.decodePacket(new PacketDecoder(identifier, senderIdentifier, publicSpace));
				
				localSpace.put(GameplayNetworkManager.PACKET_TO_HANDLE, senderIdentifier, packet);
			}
		} catch (InterruptedException e) {
			// We have been interrupted by the main thread.
		}
	}
}

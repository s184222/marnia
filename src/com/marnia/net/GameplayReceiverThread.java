package com.marnia.net;

import java.util.UUID;

import org.jspace.ActualField;
import org.jspace.Space;

import com.g4mesoft.net.NetworkManager;
import com.marnia.net.packet.INetworkHandler;
import com.marnia.net.packet.IPacket;
import com.marnia.util.SpaceHelper;

public class GameplayReceiverThread<H extends INetworkHandler> extends Thread {

	private final GameplayNetworkManager<H> manager;
	private final Space publicSpace;
	private final Space localSpace;
	private final UUID identifier;
	
	private final ActualField idMatch;
	
	public GameplayReceiverThread(GameplayNetworkManager<H> manager, Space publicSpace, Space localSpace, UUID identifier) {
		this.manager = manager;
		this.publicSpace = publicSpace;
		this.localSpace = localSpace;
		this.identifier = identifier;
		
		idMatch = new ActualField(identifier);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Object[] packetTypeInfo = publicSpace.get(idMatch, SpaceHelper.UUID_MATCH, SpaceHelper.INTEGER_MATCH);
				
				UUID senderIdentifier = (UUID)packetTypeInfo[1];
				int packetType = ((Integer)packetTypeInfo[2]).intValue();
			
				IPacket<H> packet = manager.getPacketByType(packetType);
				packet.loadPacket(publicSpace);
				
				localSpace.put(GameplayNetworkManager.PACKET_TO_HANDLE, senderIdentifier, packet);
			} catch (InterruptedException e) {
			}
		}
	}
}

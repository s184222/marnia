package com.marnia.net;

import java.util.UUID;

import org.jspace.Space;

import com.marnia.net.packet.INetworkHandler;
import com.marnia.net.packet.IPacket;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.util.SpaceHelper;

public class GameplaySenderThread<H extends INetworkHandler> extends GameplayNetworkThread<H> {

	public GameplaySenderThread(GameplayNetworkManager<H> manager, Space publicSpace, Space localSpace, UUID identifier) {
		super(manager, publicSpace, localSpace, identifier, "Gameplay sender thread");
	}

	@Override
	protected void processPacket() throws InterruptedException {
		Object[] packetToSendInfo = localSpace.get(GameplayNetworkManager.PACKET_TO_SEND_MATCH, 
				SpaceHelper.UUID_MATCH, GameplayNetworkManager.PACKET_CLASS_MATCH);
	
		UUID receiverUUID = (UUID)packetToSendInfo[1];
		IPacket<?> packetToSend = (IPacket<?>)packetToSendInfo[2];
	
		int packetType = manager.getPacketType(packetToSend);
		if (packetType != -1) {
			packetToSend.encodePacket(new PacketEncoder(identifier, receiverUUID, publicSpace));
			publicSpace.put(receiverUUID, identifier, packetType);
		} else {
			System.err.println("Attempted to send a non-registered packet " + packetToSend.getClass());
		}
	}
}

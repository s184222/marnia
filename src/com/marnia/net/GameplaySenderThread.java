package com.marnia.net;

import java.util.List;
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

	private boolean sendPacket(UUID receiverUUID, IPacket<?> packetToSend) {
		int packetType = manager.getPacketType(packetToSend);
		if (packetType != -1) {
			try {
				packetToSend.encodePacket(new PacketEncoder(receiverUUID, identifier, publicSpace));
				publicSpace.put(receiverUUID, identifier, packetType);
			} catch (InterruptedException e) {
			} catch (Exception e) {
				return false;
			}
		} else {
			System.err.println("Attempted to send a non-registered packet " + packetToSend.getClass());
		}
		
		return true;
	}
	
	@Override
	protected boolean processPacket() throws InterruptedException {
		List<Object[]> packetsInfo = localSpace.getAll(GameplayNetworkManager.PACKET_TO_SEND_MATCH, 
				SpaceHelper.UUID_MATCH, GameplayNetworkManager.PACKET_CLASS_MATCH);

		for (Object[] packetToSendInfo : packetsInfo) {
			UUID receiverUUID = (UUID)packetToSendInfo[1];
			IPacket<?> packetToSend = (IPacket<?>)packetToSendInfo[2];
			if (!sendPacket(receiverUUID, packetToSend))
				return false;
		}
		
		return true;
	}
}

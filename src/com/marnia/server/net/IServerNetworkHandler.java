package com.marnia.server.net;

import java.util.UUID;

import com.marnia.net.packet.INetworkHandler;
import com.marnia.server.net.packet.S02PlayerPositionPacket;
import com.marnia.server.net.packet.S07EnterDoorPacket;
import com.marnia.server.net.packet.S10PlayerDeathPacket;

public interface IServerNetworkHandler extends INetworkHandler {
	
	public void onPlayerPosition(UUID senderIdentifier, S02PlayerPositionPacket packet);

	public void onEnterDoorPacket(UUID senderIdentifier, S07EnterDoorPacket unlockDoorPacket);

	public void onPlayerDeathPacket(UUID senderIdentifier, S10PlayerDeathPacket playerDeathPacket);

}

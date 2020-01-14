package com.marnia.server.net;

import java.util.UUID;

import com.marnia.net.packet.INetworkHandler;
import com.marnia.server.net.packet.S02PlayerPositionPacket;

public interface IServerNetworkHandler extends INetworkHandler {
	
	public void onPlayerPosition(UUID senderIdentifier, S02PlayerPositionPacket packet);

}

package com.marnia.server.net;

import java.util.UUID;

import org.jspace.Space;

import com.marnia.net.GameplayNetworkManager;
import com.marnia.net.NetworkSide;
import com.marnia.net.PacketRegistry;
import com.marnia.net.packet.IPacket;

public class ServerGameplayNetworkManager extends GameplayNetworkManager<IServerNetworkHandler> 
	implements IServerNetworkHandler {

	public ServerGameplayNetworkManager(Space publicGameplaySpace, UUID identifier, PacketRegistry registry) {
		super(publicGameplaySpace, identifier, registry);
	}

	@Override
	protected void handlePacket(UUID sender, IPacket<IServerNetworkHandler> packet) {
		// TODO: add some check for client validity..
		
		packet.handlePacket(this);
	}
	
	@Override
	public NetworkSide getNetworkSide() {
		return NetworkSide.SERVER;
	}
}

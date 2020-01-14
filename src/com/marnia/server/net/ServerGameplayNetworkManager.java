package com.marnia.server.net;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jspace.Space;

import com.marnia.net.GameplayNetworkManager;
import com.marnia.net.NetworkSide;
import com.marnia.net.PacketRegistry;
import com.marnia.net.packet.IPacket;
import com.marnia.server.GameplaySession;

public class ServerGameplayNetworkManager extends GameplayNetworkManager<IServerNetworkHandler> {

	private final Map<UUID, GameplaySession> sessions;
	
	public ServerGameplayNetworkManager(Space publicGameplaySpace, UUID identifier, PacketRegistry registry) {
		super(publicGameplaySpace, identifier, registry);
		
		sessions = new HashMap<UUID, GameplaySession>();
	}

	public void addPlayer(UUID identifier, GameplaySession session) {
		sessions.put(identifier, session);
	}
	
	@Override
	protected void handlePacket(UUID sender, IPacket<IServerNetworkHandler> packet) {
		GameplaySession session = sessions.get(sender);
		if (session != null)
			packet.handlePacket(sender, session);
	}
	
	@Override
	public NetworkSide getNetworkSide() {
		return NetworkSide.SERVER;
	}
}

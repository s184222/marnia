package com.marnia.client.net;

import java.util.UUID;

import org.jspace.Space;

import com.marnia.client.net.packet.C01AddPlayersPacket;
import com.marnia.client.world.ClientMarniaWorld;
import com.marnia.client.ClientMarniaApp;
import com.marnia.client.net.packet.C00WorldDataPacket;
import com.marnia.net.GameplayNetworkManager;
import com.marnia.net.NetworkSide;
import com.marnia.net.PacketRegistry;
import com.marnia.net.packet.IPacket;

public class ClientGameplayNetworkManager extends GameplayNetworkManager<IClientNetworkHandler> 
	implements IClientNetworkHandler {

	private final ClientMarniaApp app;
	private final UUID serverIdentifier;
	
	public ClientGameplayNetworkManager(ClientMarniaApp app, UUID serverIdentifier, Space publicGameplaySpace, UUID identifier, PacketRegistry registry) {
		super(publicGameplaySpace, identifier, registry);
		
		this.app = app;
		this.serverIdentifier = serverIdentifier;
	}

	@Override
	protected void handlePacket(UUID sender, IPacket<IClientNetworkHandler> packet) {
		if (sender.equals(serverIdentifier))
			packet.handlePacket(this);
	}
	
	@Override
	public void onWorldDataPacket(C00WorldDataPacket worldDataPacket) {
		ClientMarniaWorld world = app.getWorld();
		if (world != null)
			world.setWorldStorage(worldDataPacket.getStorage());
	}
	
	@Override
	public void onAddPlayersPacket(C01AddPlayersPacket addPlayersPacket) {
		
	}
	
	@Override
	public NetworkSide getNetworkSide() {
		return NetworkSide.CLIENT;
	}
}

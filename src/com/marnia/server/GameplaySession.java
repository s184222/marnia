package com.marnia.server;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.marnia.client.net.IClientNetworkHandler;
import com.marnia.client.net.packet.C01AddPlayersPacket;
import com.marnia.client.net.packet.C00WorldDataPacket;
import com.marnia.net.packet.IPacket;
import com.marnia.server.net.ServerGameplayNetworkManager;
import com.marnia.server.world.ServerMarniaWorld;
import com.marnia.server.world.gen.WorldLoader;
import com.marnia.world.WorldStorage;

public class GameplaySession {

	private final ServerGameplayNetworkManager networkManager;
	private final List<GameplayProfile> profiles;

	private final ServerMarniaWorld world;
	private boolean started;
	
	public GameplaySession(ServerGameplayNetworkManager networkManager, List<GameplayProfile> profiles) {
		this.networkManager = networkManager;
		this.profiles = profiles;
		
		world = new ServerMarniaWorld();
	}
	
	public boolean startGame() {
		WorldStorage storage = null;
		try {
			storage = WorldLoader.loadFromFile("/worlds/world1.csv");
		} catch (IOException e) {
		}
		
		if (storage == null)
			return false;

		world.setWorldStorage(storage);

		sendPacketToAll(new C00WorldDataPacket(storage));
		sendPacketToAll(new C01AddPlayersPacket(profiles.toArray(new UUID[0])));

		started = true;
		
		return true;
	}
	
	public void tick() {
		if (started)
			world.tick();
	}
	
	private void sendPacketToAll(IPacket<IClientNetworkHandler> packet) {
		for (GameplayProfile profile : profiles)
			networkManager.sendPacket(packet, profile.getIdentifier());
	}
}

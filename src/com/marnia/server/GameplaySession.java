package com.marnia.server;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.marnia.client.net.IClientNetworkHandler;
import com.marnia.client.net.packet.C00WorldDataPacket;
import com.marnia.client.net.packet.C03EntityPositionPacket;
import com.marnia.entity.Entity;
import com.marnia.entity.PlayerEntity;
import com.marnia.net.packet.IPacket;
import com.marnia.server.net.IServerNetworkHandler;
import com.marnia.server.net.ServerGameplayNetworkManager;
import com.marnia.server.net.packet.S02PlayerPositionPacket;
import com.marnia.server.world.ServerMarniaWorld;
import com.marnia.server.world.gen.WorldLoader;
import com.marnia.world.WorldStorage;

public class GameplaySession implements IServerNetworkHandler {

	private final ServerGameplayNetworkManager networkManager;
	private final List<GameplayProfile> profiles;

	private final ServerMarniaWorld world;
	private boolean started;
	
	public GameplaySession(ServerGameplayNetworkManager networkManager, List<GameplayProfile> profiles) {
		this.networkManager = networkManager;
		this.profiles = profiles;
		
		world = new ServerMarniaWorld(this);
	}
	
	public boolean startGame() {
		WorldStorage storage = null;
		try {
			storage = WorldLoader.loadFromFile("/worlds/world2.csv");
		} catch (IOException e) {
		}
		
		if (storage == null)
			return false;

		world.setWorldStorage(storage);

		sendPacketToAll(new C00WorldDataPacket(storage));

		started = true;
		
		for (GameplayProfile profile : profiles)
			world.addEntity(new PlayerEntity(world, profile.getIdentifier()));
		
		return true;
	}
	
	public void tick() {
		if (started)
			world.tick();
	}
	
	public void sendPacketToAll(IPacket<IClientNetworkHandler> packet) {
		for (GameplayProfile profile : profiles)
			networkManager.sendPacket(packet, profile.getIdentifier());
	}
	
	public void sendPacketToAllExcept(IPacket<IClientNetworkHandler> packet, UUID excludeIdentifier) {
		for (GameplayProfile profile : profiles) {
			if(!profile.getIdentifier().equals(excludeIdentifier))
				networkManager.sendPacket(packet, profile.getIdentifier());
		}
	}

	public List<GameplayProfile> getProfiles() {
		return Collections.unmodifiableList(profiles);
	}
	
	@Override
	public void onPlayerPosition(UUID senderIdentifier, S02PlayerPositionPacket packet) {
		Entity currentEntity = world.getEntity(senderIdentifier);
		if (currentEntity != null)
			currentEntity.pos.set(packet.getX(), packet.getY());
		
		sendPacketToAllExcept(new C03EntityPositionPacket(packet.getX(), 
				packet.getY(), senderIdentifier), senderIdentifier);
	}
}

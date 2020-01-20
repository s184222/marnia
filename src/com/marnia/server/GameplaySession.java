package com.marnia.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.marnia.client.net.IClientNetworkHandler;
import com.marnia.client.net.packet.C00WorldDataPacket;
import com.marnia.client.net.packet.C03EntityPositionPacket;
import com.marnia.entity.Entity;
import com.marnia.entity.PlayerColor;
import com.marnia.entity.PlayerEntity;
import com.marnia.entity.registry.EntityRegistry;
import com.marnia.net.packet.IPacket;
import com.marnia.server.net.IServerNetworkHandler;
import com.marnia.server.net.ServerGameplayNetworkManager;
import com.marnia.server.net.packet.S02PlayerPositionPacket;
import com.marnia.server.world.ServerMarniaWorld;
import com.marnia.server.world.gen.WorldEntityInfo;
import com.marnia.server.world.gen.WorldFile;
import com.marnia.server.world.gen.WorldLoader;

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
		WorldFile worldFile = null;
		try {
			worldFile = WorldLoader.loadFromFile("/worlds/world1.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (worldFile == null)
			return false;

		world.setWorldStorage(worldFile.getStorage());

		sendPacketToAll(new C00WorldDataPacket(worldFile.getStorage()));

		started = true;
		
		List<PlayerColor> colors = new ArrayList<PlayerColor>();
		for (PlayerColor color : PlayerColor.values())
			colors.add(color);
		Collections.shuffle(colors);
		
		for (int i = 0; i < profiles.size(); i++) {
			UUID identifier = profiles.get(i).getIdentifier();
			world.addEntity(new PlayerEntity(world, identifier, colors.get(i)));
		}

		EntityRegistry registry = EntityRegistry.getInstance();
		for (WorldEntityInfo entityInfo : worldFile.getEntityInfos())
			world.addEntity(registry.getEntity(entityInfo.getTypeId(), world, entityInfo.getContainer(), true));

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
			currentEntity.moveToImmediately(packet.getX(), packet.getY(), false);
		
		sendPacketToAllExcept(new C03EntityPositionPacket(currentEntity), senderIdentifier);
	}
}

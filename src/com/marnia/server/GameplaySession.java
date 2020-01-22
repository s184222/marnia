package com.marnia.server;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.marnia.client.net.IClientNetworkHandler;
import com.marnia.client.net.packet.C03EntityPositionPacket;
import com.marnia.client.net.packet.C05DoorUnlockedPacket;
import com.marnia.decorations.Decoration;
import com.marnia.entity.DoorEntity;
import com.marnia.entity.Entity;
import com.marnia.entity.KeyEntity;
import com.marnia.entity.PlayerEntity;
import com.marnia.entity.registry.EntityRegistry;
import com.marnia.net.packet.IPacket;
import com.marnia.server.net.IServerNetworkHandler;
import com.marnia.server.net.ServerGameplayNetworkManager;
import com.marnia.server.net.packet.S02PlayerPositionPacket;
import com.marnia.server.net.packet.S07EnterDoorPacket;
import com.marnia.server.net.packet.S10PlayerDeathPacket;
import com.marnia.server.world.ServerMarniaWorld;
import com.marnia.server.world.gen.WorldEntityInfo;
import com.marnia.server.world.gen.WorldFile;
import com.marnia.server.world.gen.WorldLoader;
import com.marnia.world.MarniaWorld;

public class GameplaySession implements IServerNetworkHandler {

	private static final String[] WORLD_PATHS = {
		"/worlds/world1.csv",
		"/worlds/world2.csv",
		"/worlds/world3.csv"
	};
	
	private static final float MAX_DEATH_DIST = 5.0f;
	
	private final ServerGameplayNetworkManager networkManager;
	private final List<GameplayProfile> profiles;
	private final Map<UUID, ServerMarniaWorld> playerWorlds;

	private final ServerMarniaWorld[] worlds;
	private boolean started;
	
	public GameplaySession(ServerGameplayNetworkManager networkManager, List<GameplayProfile> profiles) {
		this.networkManager = networkManager;
		this.profiles = profiles;
		
		worlds = new ServerMarniaWorld[WORLD_PATHS.length];
		playerWorlds = new HashMap<UUID, ServerMarniaWorld>();
	}
	
	public boolean startGame() {
		for (int i = 0; i < WORLD_PATHS.length; i++) {
			worlds[i] = new ServerMarniaWorld(this, i);
			if (!loadWorld(worlds[i], WORLD_PATHS[i]))
				return false;
		}

		for (GameplayProfile profile : profiles)
			switchProfileWorld(profile, 0);

		started = true;

		return true;
	}
	
	private boolean loadWorld(ServerMarniaWorld world, String path) {
		WorldFile worldFile = null;
		try {
			worldFile = WorldLoader.loadFromFile(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (worldFile == null)
			return false;

		world.setWorldStorage(worldFile.getStorage());
		
		EntityRegistry registry = EntityRegistry.getInstance();
		for (WorldEntityInfo entityInfo : worldFile.getEntityInfos())
			world.addEntity(registry.getEntity(entityInfo.getTypeId(), world, entityInfo.getContainer(), true));
		for (Decoration decoration : worldFile.getDecorations())
			world.addDecoration(decoration);
		
		return true;
	}

	private void switchProfileWorld(GameplayProfile profile, int worldIndex) {
		UUID identifier = profile.getIdentifier();

		ServerMarniaWorld oldWorld = getPlayerWorld(identifier);
		if (oldWorld != null) {
			Entity playerEntity = oldWorld.getEntity(identifier);
			oldWorld.removeEntity(playerEntity);
		}
		
		playerWorlds.remove(identifier);
		
		ServerMarniaWorld world = worlds[worldIndex];
		playerWorlds.put(identifier, world);
		world.sendWorldInfo(identifier);
		world.addEntity(new PlayerEntity(world, identifier, profile.getColor()));
	}
	
	public void tick() {
		if (started) {
			for (ServerMarniaWorld world : worlds)
				world.tick();
		}
	}
	
	public void sendPacket(IPacket<IClientNetworkHandler> packet, GameplayProfile profile) {
		sendPacket(packet, profile.getIdentifier());
	}

	public void sendPacket(IPacket<IClientNetworkHandler> packet, UUID identifier) {
		networkManager.sendPacket(packet, identifier);
	}
	
	public void sendPacketToAll(IPacket<IClientNetworkHandler> packet, MarniaWorld world) {
		for (GameplayProfile profile : profiles) {
			UUID identifier = profile.getIdentifier();
			if (getPlayerWorld(identifier) == world)
				networkManager.sendPacket(packet, identifier);
		}
	}
	
	public void sendPacketToAllExcept(IPacket<IClientNetworkHandler> packet, MarniaWorld world, UUID excludeIdentifier) {
		for (GameplayProfile profile : profiles) {
			UUID identifier = profile.getIdentifier();
			if(getPlayerWorld(identifier) == world && !identifier.equals(excludeIdentifier))
				networkManager.sendPacket(packet, profile.getIdentifier());
		}
	}

	public List<GameplayProfile> getProfiles() {
		return Collections.unmodifiableList(profiles);
	}
	
	public ServerMarniaWorld getPlayerWorld(UUID playerIdentifier) {
		return playerWorlds.get(playerIdentifier);
	}
	
	private GameplayProfile getProfile(UUID identifier) {
		for (GameplayProfile profile : profiles) {
			if (profile.getIdentifier().equals(identifier))
				return profile;
		}
		return null;
	}
	
	@Override
	public void onPlayerPosition(UUID senderIdentifier, S02PlayerPositionPacket packet) {
		ServerMarniaWorld world = getPlayerWorld(senderIdentifier);
		if (world == null)
			return;
		
		Entity currentEntity = world.getEntity(senderIdentifier);
		if (currentEntity != null) {
			currentEntity.moveToImmediately(packet.getX(), packet.getY(), false);
			sendPacketToAllExcept(new C03EntityPositionPacket(currentEntity), world, senderIdentifier);
		}
	}

	@Override
	public void onEnterDoorPacket(UUID senderIdentifier, S07EnterDoorPacket unlockDoorPacket) {
		ServerMarniaWorld world = getPlayerWorld(senderIdentifier);
		if (world == null)
			return;
		
		Entity doorEntity = world.getEntity(unlockDoorPacket.getDoorIdentifier());
		Entity playerEntity = world.getEntity(senderIdentifier);
		
		if (doorEntity instanceof DoorEntity && playerEntity instanceof PlayerEntity) {
			DoorEntity door = (DoorEntity)doorEntity;
			PlayerEntity player = (PlayerEntity)playerEntity;

			if (door.getHitbox().collides(player.getHitbox())) {
				if (!door.isUnlocked()) {
					Iterator<UUID> keyIdentifiers = player.getKeyIdentifiers().iterator();
					if (keyIdentifiers.hasNext()) {
						Entity keyEntity = world.getEntity(keyIdentifiers.next());
						if (keyEntity instanceof KeyEntity) {
							world.removeEntity(keyEntity);
							door.setUnlocked(true);
							
							sendPacketToAll(new C05DoorUnlockedPacket(door), world);
						}
					}
				} else {
					int index = world.getWorldIndex();
					if (index + 1 < worlds.length) {
						GameplayProfile profile = getProfile(player.getIdentifier());
						if (profile != null)
							switchProfileWorld(profile, index + 1);
					}
				}
			}
		}
	}

	@Override
	public void onPlayerDeathPacket(UUID senderIdentifier, S10PlayerDeathPacket playerDeathPacket) {
		ServerMarniaWorld world = getPlayerWorld(senderIdentifier);
		if (world == null)
			return;
		
		Entity playerEntity = world.getEntity(senderIdentifier);
		Entity causeEntity = world.getEntity(playerDeathPacket.getCauseIdentifier());
		if (playerEntity instanceof PlayerEntity && causeEntity != null) {
			PlayerEntity player = (PlayerEntity)playerEntity;
			float distX = player.getCenterX() - causeEntity.getCenterX();
			float distY = player.getCenterY() - causeEntity.getCenterY();
			float distSqr = distX * distX + distY * distY;
			
			if (distSqr < MAX_DEATH_DIST * MAX_DEATH_DIST)
				player.respawnAtCheckpoint();
		}
	}
}

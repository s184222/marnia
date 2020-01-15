package com.marnia.client.net;

import java.util.UUID;

import org.jspace.Space;

import com.marnia.client.ClientMarniaApp;
import com.marnia.client.net.packet.C00WorldDataPacket;
import com.marnia.client.net.packet.C01AddEntityPacket;
import com.marnia.client.net.packet.C03EntityPositionPacket;
import com.marnia.client.world.ClientMarniaWorld;
import com.marnia.entity.Entity;
import com.marnia.entity.PlayerEntity;
import com.marnia.entity.registry.EntityRegistry;
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
		if (serverIdentifier.equals(sender))
			packet.handlePacket(sender, this);
	}

	public void sendPacket(IPacket<?> packet) {
		sendPacket(packet, serverIdentifier);
	}
	
	@Override
	public void sendPacket(IPacket<?> packet, UUID receiver) {
		if (!serverIdentifier.equals(receiver))
			throw new IllegalArgumentException("Receiver must be server identifier!");
		
		super.sendPacket(packet, receiver);
	}
	
	@Override
	public void onWorldDataPacket(C00WorldDataPacket worldDataPacket) {
		ClientMarniaWorld world = app.getWorld();
		if (world != null)
			world.setWorldStorage(worldDataPacket.getStorage());
	}
	
	@Override
	public void onAddEntityPacket(C01AddEntityPacket addEntityPacket) {
		ClientMarniaWorld world = app.getWorld();
		Entity entity = EntityRegistry.getInstance().getEntity(addEntityPacket.getEntityId(), 
				world, addEntityPacket.getEntityContainer());
		
		if (entity != null) {
			Entity currentEntity = world.getEntity(entity.identifier);
			if (currentEntity != null)
				world.removeEntity(currentEntity);
			
			// This is our player entity.
			if (entity instanceof PlayerEntity && entity.identifier.equals(app.getIdentifier())) {
				entity.setController(app.getClientController());
				app.setPlayerEntity((PlayerEntity)entity);
			}
			
			world.addEntity(entity);
		}
	}
	
	@Override
	public void onEntityPositionPacket(C03EntityPositionPacket positionPacket) {
		Entity entity = app.getWorld().getEntity(positionPacket.getIdentifier());
		if (entity != null)
			entity.moveToImmediately(positionPacket.getX(), positionPacket.getY());
	}
	
	@Override
	public NetworkSide getNetworkSide() {
		return NetworkSide.CLIENT;
	}
}
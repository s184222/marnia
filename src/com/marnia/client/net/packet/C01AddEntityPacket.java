package com.marnia.client.net.packet;

import java.io.IOException;
import java.util.UUID;

import com.marnia.client.net.IClientNetworkHandler;
import com.marnia.entity.Entity;
import com.marnia.entity.registry.EntityContainer;
import com.marnia.entity.registry.EntityRegistry;
import com.marnia.entity.registry.IEntityProvider;
import com.marnia.net.packet.IPacket;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.util.SpaceHelper;

public class C01AddEntityPacket implements IPacket<IClientNetworkHandler> {

	private int entityId;
	private EntityContainer container;
	
	public C01AddEntityPacket() {
	}

	public C01AddEntityPacket(Entity entity) {
		EntityRegistry entityRegistry = EntityRegistry.getInstance();
		entityId = entityRegistry.getEntityId(entity);
		
		@SuppressWarnings("rawtypes")
		IEntityProvider provider = entityRegistry.getEntityProvider(entityId);
		@SuppressWarnings("unchecked")
		EntityContainer container = provider.getContainer(entity);
		
		this.container = container;
	}

	public C01AddEntityPacket(int entityId, EntityContainer container) {
		this.entityId = entityId;
		this.container = container;
	}
	
	@Override
	public void decodePacket(PacketDecoder decoder) throws InterruptedException, IOException {
		decoder.fetchData(SpaceHelper.INTEGER_MATCH);
		entityId = decoder.getData(Integer.class, 0);

		IEntityProvider<?, ?> registry = EntityRegistry.getInstance().getEntityProvider(entityId);
		if (registry == null)
			throw new IOException("Registry with id not found: " + entityId);
		container = registry.decodeContainer(decoder);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void encodePacket(PacketEncoder encoder) throws InterruptedException {
		encoder.putData(entityId);
		
		IEntityProvider registry = EntityRegistry.getInstance().getEntityProvider(entityId);
		registry.encodeContainer(container, encoder);
	}

	@Override
	public void handlePacket(UUID senderIdentifier, IClientNetworkHandler handler) {
		handler.onAddEntityPacket(this);
	}
	
	public int getEntityId() {
		return entityId;
	}
	
	public EntityContainer getEntityContainer() {
		return container;
	}
}

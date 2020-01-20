package com.marnia.client.net.packet;

import java.io.IOException;
import java.util.UUID;

import com.marnia.client.net.IClientNetworkHandler;
import com.marnia.entity.Entity;
import com.marnia.net.packet.IPacket;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.util.SpaceHelper;

public class C06RemoveEntityPacket implements IPacket<IClientNetworkHandler> {

	private UUID entityIdentifier;
	
	public C06RemoveEntityPacket() {
	}

	public C06RemoveEntityPacket(Entity entity) {
		this(entity.getIdentifier());
	}

	public C06RemoveEntityPacket(UUID entityIdentifier) {
		this.entityIdentifier = entityIdentifier;
	}
	
	@Override
	public void decodePacket(PacketDecoder decoder) throws InterruptedException, IOException {
		decoder.fetchData(SpaceHelper.UUID_MATCH);
		entityIdentifier = decoder.getData(UUID.class, 0);
	}

	@Override
	public void encodePacket(PacketEncoder encoder) throws InterruptedException {
		encoder.putData(entityIdentifier);
	}

	@Override
	public void handlePacket(UUID senderIdentifier, IClientNetworkHandler handler) {
		handler.onRemoveEntityPacket(this);
	}

	public UUID getEntityIdentifier() {
		return entityIdentifier;
	}
}

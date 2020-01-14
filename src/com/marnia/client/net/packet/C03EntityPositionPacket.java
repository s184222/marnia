package com.marnia.client.net.packet;

import java.io.IOException;
import java.util.UUID;

import com.marnia.client.net.IClientNetworkHandler;
import com.marnia.net.packet.IPacket;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.util.SpaceHelper;

public class C03EntityPositionPacket implements IPacket<IClientNetworkHandler> {
	
	private float x;
	private float y;
	
	private UUID identifier;
	
	public C03EntityPositionPacket() {
	}
	
	public C03EntityPositionPacket(float x, float y, UUID identifier) {
		this.x = x;
		this.y = y;
		this.identifier = identifier;
	}

	@Override
	public void decodePacket(PacketDecoder decoder) throws InterruptedException, IOException {
		decoder.fetchData(SpaceHelper.FLOAT_MATCH, SpaceHelper.FLOAT_MATCH, SpaceHelper.UUID_MATCH);
		x = decoder.getData(Float.class, 0);
		y = decoder.getData(Float.class, 1);
		identifier = decoder.getData(UUID.class, 2);
	}

	@Override
	public void encodePacket(PacketEncoder encoder) throws InterruptedException {
		encoder.putData(x, y, identifier);
	}

	@Override
	public void handlePacket(UUID senderIdentifier, IClientNetworkHandler handler) {
		handler.onEntityPosition(this);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}

	public UUID getIdentifier() {
		return identifier;
	}
}

package com.marnia.client.net.packet;

import java.io.IOException;
import java.util.UUID;

import com.marnia.client.net.IClientNetworkHandler;
import com.marnia.entity.DoorEntity;
import com.marnia.net.packet.IPacket;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.util.SpaceHelper;

public class C05DoorUnlockedPacket implements IPacket<IClientNetworkHandler> {

	private UUID doorIdentifier;
	
	public C05DoorUnlockedPacket() {
	}

	public C05DoorUnlockedPacket(DoorEntity door) {
		this(door.getIdentifier());
	}

	public C05DoorUnlockedPacket(UUID doorIdentifier) {
		this.doorIdentifier = doorIdentifier;
	}
	
	@Override
	public void decodePacket(PacketDecoder decoder) throws InterruptedException, IOException {
		decoder.fetchData(SpaceHelper.UUID_MATCH);
		doorIdentifier = decoder.getData(UUID.class, 0);
	}

	@Override
	public void encodePacket(PacketEncoder encoder) throws InterruptedException {
		encoder.putData(doorIdentifier);
	}

	@Override
	public void handlePacket(UUID senderIdentifier, IClientNetworkHandler handler) {
		handler.onDoorUnlockedPacket(this);
	}

	public UUID getDoorIdentifier() {
		return doorIdentifier;
	}
}

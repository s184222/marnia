package com.marnia.server.net.packet;

import java.io.IOException;
import java.util.UUID;

import com.marnia.entity.DoorEntity;
import com.marnia.net.packet.IPacket;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.server.net.IServerNetworkHandler;
import com.marnia.util.SpaceHelper;

public class S07EnterDoorPacket implements IPacket<IServerNetworkHandler> {

	private UUID doorIdentifier;
	
	public S07EnterDoorPacket() {
	}

	public S07EnterDoorPacket(DoorEntity door) {
		this(door.getIdentifier());
	}

	public S07EnterDoorPacket(UUID doorIdentifier) {
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
	public void handlePacket(UUID senderIdentifier, IServerNetworkHandler handler) {
		handler.onEnterDoorPacket(senderIdentifier, this);
	}
	
	public UUID getDoorIdentifier() {
		return doorIdentifier;
	}
}

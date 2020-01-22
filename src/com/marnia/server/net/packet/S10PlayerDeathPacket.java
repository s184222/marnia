package com.marnia.server.net.packet;

import java.io.IOException;
import java.util.UUID;

import com.marnia.entity.Entity;
import com.marnia.net.packet.IPacket;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.server.net.IServerNetworkHandler;
import com.marnia.util.SpaceHelper;

public class S10PlayerDeathPacket implements IPacket<IServerNetworkHandler> {

	private UUID causeIdentifier;

	public S10PlayerDeathPacket() {
	}

	public S10PlayerDeathPacket(Entity cause) {
		this(cause.getIdentifier());
	}
	
	public S10PlayerDeathPacket(UUID causeIdentifier) {
		this.causeIdentifier = causeIdentifier;
	}

	@Override
	public void decodePacket(PacketDecoder decoder) throws InterruptedException, IOException {
		decoder.fetchData(SpaceHelper.UUID_MATCH);
		causeIdentifier = decoder.getData(UUID.class, 0);
	}

	@Override
	public void encodePacket(PacketEncoder encoder) throws InterruptedException {
		encoder.putData(causeIdentifier);
	}

	@Override
	public void handlePacket(UUID senderIdentifier, IServerNetworkHandler handler) {
		handler.onPlayerDeathPacket(senderIdentifier, this);
	}
	
	public UUID getCauseIdentifier() {
		return causeIdentifier;
	}
}

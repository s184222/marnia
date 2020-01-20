package com.marnia.client.net.packet;

import java.io.IOException;
import java.util.UUID;

import com.marnia.client.net.IClientNetworkHandler;
import com.marnia.entity.KeyEntity;
import com.marnia.entity.PlayerEntity;
import com.marnia.net.packet.IPacket;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.util.SpaceHelper;

public class C04KeyCollectedPacket implements IPacket<IClientNetworkHandler> {

	private UUID keyIdentifier;
	private UUID playerIdentifier;

	public C04KeyCollectedPacket() {
	}

	public C04KeyCollectedPacket(KeyEntity keyEntity, PlayerEntity playerEntity) {
		this(keyEntity.getIdentifier(), playerEntity.getIdentifier());
	}
	
	public C04KeyCollectedPacket(UUID keyIdentifier, UUID playerIdentifier) {
		this.keyIdentifier = keyIdentifier;
		this.playerIdentifier = playerIdentifier;
	}

	@Override
	public void decodePacket(PacketDecoder decoder) throws InterruptedException, IOException {
		decoder.fetchData(SpaceHelper.UUID_MATCH, SpaceHelper.UUID_MATCH);
		keyIdentifier = decoder.getData(UUID.class, 0);
		playerIdentifier = decoder.getData(UUID.class, 1);
	}

	@Override
	public void encodePacket(PacketEncoder encoder) throws InterruptedException {
		encoder.putData(keyIdentifier, playerIdentifier);
	}

	@Override
	public void handlePacket(UUID senderIdentifier, IClientNetworkHandler handler) {
		handler.onKeyCollectedPacket(this);
	}
	
	public UUID getKeyIdentifier() {
		return keyIdentifier;
	}
	
	public UUID getPlayerIdentifier() {
		return playerIdentifier;
	}
}

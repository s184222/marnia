package com.marnia.client.net.packet;

import java.io.IOException;
import java.util.UUID;

import com.marnia.client.net.IClientNetworkHandler;
import com.marnia.net.packet.IPacket;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.util.SpaceHelper;

public class C01AddPlayersPacket implements IPacket<IClientNetworkHandler> {

	private UUID[] playerIdentifiers;
	
	public C01AddPlayersPacket() {
	}

	public C01AddPlayersPacket(UUID[] playerIdentifiers) {
		this.playerIdentifiers = playerIdentifiers;
	}
	
	@Override
	public void decodePacket(PacketDecoder decoder) throws InterruptedException, IOException {
		decoder.fetchData(SpaceHelper.UUID_ARRAY_MATCH);
		playerIdentifiers = decoder.getData(UUID[].class, 0);
	}

	@Override
	public void encodePacket(PacketEncoder encoder) throws InterruptedException {
		encoder.putData(new Object[] { playerIdentifiers });
	}

	@Override
	public void handlePacket(UUID senderIdentifier, IClientNetworkHandler handler) {
		handler.onAddPlayersPacket(this);
	}
	
	public UUID[] getIdentifiers() {
		return playerIdentifiers;
	}
}

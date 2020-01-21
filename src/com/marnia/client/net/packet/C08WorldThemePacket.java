package com.marnia.client.net.packet;

import java.io.IOException;
import java.util.UUID;

import com.marnia.client.net.IClientNetworkHandler;
import com.marnia.net.packet.IPacket;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.util.SpaceHelper;
import com.marnia.world.WorldTheme;

public class C08WorldThemePacket implements IPacket<IClientNetworkHandler> {

	private WorldTheme theme;
	
	public C08WorldThemePacket() {
	}

	public C08WorldThemePacket(WorldTheme theme) {
		this.theme = theme;
	}
	
	@Override
	public void decodePacket(PacketDecoder decoder) throws InterruptedException, IOException {
		decoder.fetchData(SpaceHelper.INTEGER_MATCH);
		theme = WorldTheme.fromIndex(decoder.getData(Integer.class, 0));
		if (theme == null)
			throw new IOException("World theme is invalid.");
	}

	@Override
	public void encodePacket(PacketEncoder encoder) throws InterruptedException {
		encoder.putData(theme.getIndex());
	}

	@Override
	public void handlePacket(UUID senderIdentifier, IClientNetworkHandler handler) {
		handler.onWorldThemePacket(this);
	}
	
	public WorldTheme getTheme() {
		return theme;
	}
}

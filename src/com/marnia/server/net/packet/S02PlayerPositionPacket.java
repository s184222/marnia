package com.marnia.server.net.packet;

import java.io.IOException;
import java.util.UUID;

import com.marnia.net.packet.IPacket;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.server.net.IServerNetworkHandler;
import com.marnia.util.SpaceHelper;

public class S02PlayerPositionPacket implements IPacket<IServerNetworkHandler>{
	
	private float x;
	private float y;

	public S02PlayerPositionPacket() {
	}
	
	public S02PlayerPositionPacket(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void decodePacket(PacketDecoder decoder) throws InterruptedException, IOException {
		decoder.fetchData(SpaceHelper.FLOAT_MATCH, SpaceHelper.FLOAT_MATCH);
		x = decoder.getData(Float.class, 0);
		y = decoder.getData(Float.class, 1);
	}
	
	@Override
	public void encodePacket(PacketEncoder encoder) throws InterruptedException {
		encoder.putData(x, y);
	}
	
	@Override
	public void handlePacket(UUID senderIdentifier, IServerNetworkHandler handler) {
		handler.onPlayerPosition(senderIdentifier, this);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}

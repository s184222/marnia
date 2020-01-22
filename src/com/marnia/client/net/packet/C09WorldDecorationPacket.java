package com.marnia.client.net.packet;

import java.io.IOException;
import java.util.UUID;

import com.marnia.client.net.IClientNetworkHandler;
import com.marnia.decorations.Decoration;
import com.marnia.decorations.DecorationType;
import com.marnia.net.packet.IPacket;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.util.SpaceHelper;

public class C09WorldDecorationPacket implements IPacket<IClientNetworkHandler> {

	private DecorationType type;
	private float x;
	private float y;
	
	public C09WorldDecorationPacket() {
	}

	public C09WorldDecorationPacket(Decoration decoration) {
		this(decoration.getType(), decoration.getX(), decoration.getY());
	}

	public C09WorldDecorationPacket(DecorationType type, float x, float y) {
		this.type = type;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void decodePacket(PacketDecoder decoder) throws InterruptedException, IOException {
		decoder.fetchData(SpaceHelper.INTEGER_MATCH, SpaceHelper.FLOAT_MATCH, SpaceHelper.FLOAT_MATCH);
		
		type = DecorationType.fromIndex(decoder.getData(Integer.class, 0));
		if (type == null)
			throw new IOException("Invalid decoration type");
		x = decoder.getData(Float.class, 1);
		y = decoder.getData(Float.class, 2);
	}

	@Override
	public void encodePacket(PacketEncoder encoder) throws InterruptedException {
		encoder.putData(type.getIndex(), x, y);
	}

	@Override
	public void handlePacket(UUID senderIdentifier, IClientNetworkHandler handler) {
		handler.onWorldDecorationPacket(this);
	}
	
	public DecorationType getType() {
		return type;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}

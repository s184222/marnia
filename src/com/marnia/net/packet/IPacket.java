package com.marnia.net.packet;

import java.io.IOException;
import java.util.UUID;

public interface IPacket<H extends INetworkHandler> {

	public void decodePacket(PacketDecoder decoder) throws InterruptedException, IOException;
	
	public void encodePacket(PacketEncoder encoder) throws InterruptedException;
	
	public void handlePacket(UUID senderIdentifier, H handler);
	
}

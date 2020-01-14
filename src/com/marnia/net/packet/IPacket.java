package com.marnia.net.packet;

public interface IPacket<H extends INetworkHandler> {

	public void decodePacket(PacketDecoder decoder);
	
	public void writePacket(PacketEncoder encoder);
	
	public void handlePacket(H handler);
	
}

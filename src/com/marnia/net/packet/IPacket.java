package com.marnia.net.packet;

import org.jspace.Space;

public interface IPacket<H extends INetworkHandler> {

	public void loadPacket(Space space);
	
	public void writePacket(Space space);
	
	public void handlePacket(H handler);
	
}

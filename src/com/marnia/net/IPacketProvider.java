package com.marnia.net;

import com.marnia.net.packet.IPacket;

public interface IPacketProvider<P extends IPacket<?>> {

	public P getPacketInstance();
	
}

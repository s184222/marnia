package com.marnia.client.net;

import com.marnia.client.net.packet.C00WorldDataPacket;
import com.marnia.client.net.packet.C01AddEntityPacket;
import com.marnia.client.net.packet.C03EntityPositionPacket;
import com.marnia.net.packet.INetworkHandler;

public interface IClientNetworkHandler extends INetworkHandler {

	public void onWorldDataPacket(C00WorldDataPacket worldDataPacket);

	public void onAddEntityPacket(C01AddEntityPacket addEntityPacket);
	
	public void onEntityPositionPacket(C03EntityPositionPacket positionPacket);

}

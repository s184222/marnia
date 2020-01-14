package com.marnia.client.net;

import com.marnia.client.net.packet.C00WorldDataPacket;
import com.marnia.client.net.packet.C01AddPlayersPacket;
import com.marnia.client.net.packet.C03EntityPositionPacket;
import com.marnia.net.packet.INetworkHandler;

public interface IClientNetworkHandler extends INetworkHandler {

	public void onWorldDataPacket(C00WorldDataPacket worldDataPacket);

	public void onAddPlayersPacket(C01AddPlayersPacket addPlayersPacket);
	
	public void onEntityPositionPacket(C03EntityPositionPacket positionPacket);

}

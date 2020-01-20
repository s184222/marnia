package com.marnia.client.net;

import com.marnia.client.net.packet.C00SwitchWorldPacket;
import com.marnia.client.net.packet.C01AddEntityPacket;
import com.marnia.client.net.packet.C03EntityPositionPacket;
import com.marnia.client.net.packet.C04KeyCollectedPacket;
import com.marnia.client.net.packet.C05DoorUnlockedPacket;
import com.marnia.client.net.packet.C06RemoveEntityPacket;
import com.marnia.net.packet.INetworkHandler;

public interface IClientNetworkHandler extends INetworkHandler {

	public void onSwitchWorldPacket(C00SwitchWorldPacket worldDataPacket);

	public void onAddEntityPacket(C01AddEntityPacket addEntityPacket);
	
	public void onEntityPositionPacket(C03EntityPositionPacket positionPacket);

	public void onKeyCollectedPacket(C04KeyCollectedPacket keyCollectedPacket);

	public void onDoorUnlockedPacket(C05DoorUnlockedPacket doorUnlockedPacket);

	public void onRemoveEntityPacket(C06RemoveEntityPacket removeEntityPacket);

}

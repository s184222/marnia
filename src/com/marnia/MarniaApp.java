package com.marnia;

import org.jspace.SequentialSpace;
import org.jspace.Space;

import com.g4mesoft.Application;
import com.g4mesoft.graphic.DisplayConfig;
import com.marnia.client.net.packet.C00WorldDataPacket;
import com.marnia.client.net.packet.C01AddEntityPacket;
import com.marnia.client.net.packet.C03EntityPositionPacket;
import com.marnia.client.net.packet.C04KeyCollectedPacket;
import com.marnia.net.PacketRegistry;
import com.marnia.server.net.packet.S02PlayerPositionPacket;

public abstract class MarniaApp extends Application {

	protected static final String LOBBY_SPACE_NAME = "lobby";
	protected static final String GAMEPLAY_SPACE_NAME = "gameplay";
	
	protected Space lobbySpace;
	protected Space gameplaySpace;
	protected Space localSpace;
	
	protected PacketRegistry registry;
	
	public MarniaApp(DisplayConfig config) {
		super(config);
	}
	
	@Override
	public void init() {
		super.init();

		localSpace = new SequentialSpace();
		
		registry = new PacketRegistry();
		registry.addPacketType(C00WorldDataPacket.class, 0, C00WorldDataPacket::new);
		registry.addPacketType(C01AddEntityPacket.class, 1, C01AddEntityPacket::new);
		registry.addPacketType(S02PlayerPositionPacket.class, 2, S02PlayerPositionPacket::new);
		registry.addPacketType(C03EntityPositionPacket.class, 3, C03EntityPositionPacket::new);
		registry.addPacketType(C04KeyCollectedPacket.class, 4, C04KeyCollectedPacket::new);
		
		setDebug(false);
	}

	public String getGateAddress(String address, String port) {
		return getGateAddress(address, port, "");
	}
	
	public String getGateAddress(String address, String port, String spaceName) {
		return "tcp://" + address + ":" + port + "/" + spaceName + "?keep";
	}
	
	public Space getGameplaySpace() {
		return gameplaySpace;
	}

	public Space getLobbySpace() {
		return lobbySpace;
	}
}

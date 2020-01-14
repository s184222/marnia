package com.marnia.net;

import java.util.UUID;

import org.jspace.Space;

import com.marnia.net.packet.INetworkHandler;

public class GameplaySenderThread<H extends INetworkHandler> extends GameplayNetworkThread<H> {

	public GameplaySenderThread(GameplayNetworkManager<H> manager, Space publicSpace, Space localSpace, UUID identifier) {
		super(manager, publicSpace, localSpace, identifier, "Gameplay sender thread");
	}

	@Override
	public void run() {
		try {
			while (isGameplayRunning()) {
			}
		} catch (InterruptedException e) {
		}
	}
}

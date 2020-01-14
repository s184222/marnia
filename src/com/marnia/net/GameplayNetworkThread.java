package com.marnia.net;

import java.util.UUID;

import org.jspace.Space;

import com.marnia.net.packet.INetworkHandler;

public abstract class GameplayNetworkThread<H extends INetworkHandler> extends Thread {

	protected final GameplayNetworkManager<H> manager;
	protected final Space publicSpace;
	protected final Space localSpace;
	protected final UUID identifier;
	
	public GameplayNetworkThread(GameplayNetworkManager<H> manager, Space publicSpace, Space localSpace, UUID identifier, String name) {
		super(name);
		
		this.manager = manager;
		this.publicSpace = publicSpace;
		this.localSpace = localSpace;
		this.identifier = identifier;

		setDaemon(true);
	}
	
	protected boolean isGameplayRunning() throws InterruptedException {
		return localSpace.queryp(GameplayNetworkManager.RUNTIME_FIELDS_MATCH, 
				GameplayNetworkManager.FIELD_RUNNING_MATCH) != null;
	}

	protected abstract void processPacket() throws InterruptedException;
	
	@Override
	public final void run() {
		try {
			while (isGameplayRunning())
				processPacket();
		} catch (InterruptedException e) {
			// We have been interrupted by the main thread.
		}
	}
	
}

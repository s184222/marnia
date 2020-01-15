package com.marnia.server.world;

import com.marnia.client.net.packet.C01AddEntityPacket;
import com.marnia.entity.Entity;
import com.marnia.server.GameplaySession;
import com.marnia.world.MarniaWorld;

public class ServerMarniaWorld extends MarniaWorld {

	private final GameplaySession session;
	
	public ServerMarniaWorld(GameplaySession session) {
		this.session = session;
	}
	
	@Override
	public void addEntity(Entity entity) {
		super.addEntity(entity);

		session.sendPacketToAll(new C01AddEntityPacket(entity));
	}
	
	public GameplaySession getSession() {
		return session;
	}
}

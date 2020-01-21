package com.marnia.server.world;

import java.util.UUID;

import com.marnia.client.net.packet.C00SwitchWorldPacket;
import com.marnia.client.net.packet.C01AddEntityPacket;
import com.marnia.client.net.packet.C06RemoveEntityPacket;
import com.marnia.client.net.packet.C08WorldThemePacket;
import com.marnia.entity.Entity;
import com.marnia.server.GameplaySession;
import com.marnia.world.MarniaWorld;
import com.marnia.world.WorldTheme;

public class ServerMarniaWorld extends MarniaWorld {

	private final GameplaySession session;
	
	private final int worldIndex;
	
	public ServerMarniaWorld(GameplaySession session, int worldIndex) {
		super(WorldTheme.fromIndex(worldIndex % WorldTheme.values().length));
		
		this.session = session;
		
		this.worldIndex = worldIndex;
	}
	
	@Override
	public void addEntity(Entity entity) {
		super.addEntity(entity);

		session.sendPacketToAll(new C01AddEntityPacket(entity), this);
	}
	
	@Override
	public boolean removeEntity(Entity entity) {
		if (super.removeEntity(entity)) {
			session.sendPacketToAll(new C06RemoveEntityPacket(entity), this);
			return true;
		}
		return false;
	}
	
	public void sendWorldInfo(UUID identifier) {
		session.sendPacket(new C00SwitchWorldPacket(getStorage()), identifier);
		session.sendPacket(new C08WorldThemePacket(getTheme()), identifier);
		
		for (Entity entity : entities)
			session.sendPacket(new C01AddEntityPacket(entity), identifier);
	}

	@Override
	public void setTheme(WorldTheme theme) {
		super.setTheme(theme);
		
		session.sendPacketToAll(new C08WorldThemePacket(theme), this);
	}
	
	public GameplaySession getSession() {
		return session;
	}

	public int getWorldIndex() {
		return worldIndex;
	}
	
	@Override
	public boolean isServer() {
		return true;
	}
}

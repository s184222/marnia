package com.marnia.entity;

import java.util.UUID;

import com.marnia.client.net.packet.C03EntityPositionPacket;
import com.marnia.server.GameplaySession;
import com.marnia.server.world.ServerMarniaWorld;
import com.marnia.world.MarniaWorld;

public class PlayerEntity extends Entity {

	private static final int DEATH_TIME = 20;

	private int deathTimer;

	public PlayerEntity(MarniaWorld world, UUID identifier) {
		super(world, identifier);
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isServer()) {
			if (pos.y >= world.getHeight()) {
				deathTimer++;

				if (deathTimer >= DEATH_TIME)
					respawnAtCheckpoint();
			} else {
				deathTimer = 0;
			}
		}
	}

	public void respawnAtCheckpoint() {
		if (world.isServer()) {
			moveToImmediately(0.0f, 0.0f);
			GameplaySession session = ((ServerMarniaWorld)world).getSession();
			session.sendPacketToAll(new C03EntityPositionPacket(pos.x, pos.y, identifier));

			deathTimer = 0;
		}
	}
}

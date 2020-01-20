package com.marnia.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.marnia.client.net.packet.C03EntityPositionPacket;
import com.marnia.server.GameplaySession;
import com.marnia.server.world.ServerMarniaWorld;
import com.marnia.world.MarniaWorld;

public class PlayerEntity extends Entity {

	private static final int DEATH_TIME = 20;

	private final PlayerColor color;
	private final Set<UUID> keyIdentifiers;
	
	private int deathTimer;

	public PlayerEntity(MarniaWorld world, UUID identifier, PlayerColor color) {
		super(world);

		this.color = color;
		keyIdentifiers = new HashSet<UUID>();
		
		setIdentifier(identifier);
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
			moveToImmediately(0.0f, 0.0f, false);
			GameplaySession session = ((ServerMarniaWorld)world).getSession();
			session.sendPacketToAll(new C03EntityPositionPacket(this), world);

			deathTimer = 0;
		}
	}
	
	public PlayerColor getColor() {
		return color;
	}

	public void pickup(KeyEntity keyEntity) {
		keyIdentifiers.add(keyEntity.identifier);
	}

	public void loseKey(KeyEntity keyEntity) {
		keyIdentifiers.remove(keyEntity.getIdentifier());
	}
	
	public Set<UUID> getKeyIdentifiers() {
		return Collections.unmodifiableSet(keyIdentifiers);
	}
}

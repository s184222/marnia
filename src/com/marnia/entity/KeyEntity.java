package com.marnia.entity;

import java.util.UUID;

import com.marnia.client.net.packet.C04KeyCollectedPacket;
import com.marnia.server.GameplaySession;
import com.marnia.server.world.ServerMarniaWorld;
import com.marnia.world.MarniaWorld;

public class KeyEntity extends Entity {

	private UUID followIdentifier;
	
	public KeyEntity(MarniaWorld world) {
		super(world, new KeyController());
	
		followIdentifier = null;
	}

	@Override
	public void tick() {
		super.tick();
		
		if (world.isServer() && followIdentifier == null) {
			Entity closestEntity = world.getClosestEntity(this);
			if (closestEntity instanceof PlayerEntity && closestEntity.getHitbox().collides(hitbox)) {
				followIdentifier = closestEntity.identifier;
				
				PlayerEntity player = (PlayerEntity)closestEntity;
				player.pickup(this);
				
				GameplaySession session = ((ServerMarniaWorld)world).getSession();
				session.sendPacketToAll(new C04KeyCollectedPacket(this, player));
			}
		}
	}

	public void setFollowing(PlayerEntity player) {
		followIdentifier = player.identifier;
	}
	
	public UUID getFollowIdentifier() {
		return followIdentifier;
	}
}

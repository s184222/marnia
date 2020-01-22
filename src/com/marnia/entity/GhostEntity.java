package com.marnia.entity;

import com.marnia.client.ClientMarniaApp;
import com.marnia.client.world.ClientMarniaWorld;
import com.marnia.server.entity.GhostController;
import com.marnia.server.net.packet.S10PlayerDeathPacket;
import com.marnia.world.MarniaWorld;

public class GhostEntity extends Entity {

	public GhostEntity(MarniaWorld world) {
		super(world);

		if (world.isServer())
			setController(new GhostController());
	}

	@Override
	public void tick() {
		super.tick();
		
		if (!world.isServer()) {
			Entity closestEntity = world.getClosestEntity(this);
			if (closestEntity instanceof PlayerEntity) {
				if (closestEntity.getHitbox().collides(getHitbox())) {
					ClientMarniaApp app = ((ClientMarniaWorld)world).getMarniaApp();
					app.getNetworkManager().sendPacket(new S10PlayerDeathPacket(this));
				}
			}
		}
	}
}

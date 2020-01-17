package com.marnia.server.entity;

import com.g4mesoft.world.phys.AABB;
import com.marnia.client.net.packet.C03EntityPositionPacket;
import com.marnia.entity.Entity;
import com.marnia.entity.EntityDirection;
import com.marnia.entity.IController;
import com.marnia.server.world.ServerMarniaWorld;

public class GhostController implements IController {

	private static final float GHOST_SPEED = 0.1f;

	private EntityDirection direction;

	public GhostController() {
		direction = EntityDirection.RIGHT;
	}

	@Override
	public void update(Entity entity) {
		if(entity.hitHorizontalHitbox()){
			direction = direction.getOpposite();
		} else if (entity.getTileBelow().isSolidTop()) {
			AABB hitbox = entity.getHitbox();

			float xFuture = (direction == EntityDirection.LEFT) ? hitbox.x0 : hitbox.x1;
			xFuture += direction.getXOffset() * GHOST_SPEED;

			int ytBelow = (int)(hitbox.y1 + 0.1f);
			if (!entity.world.getTile((int)xFuture, ytBelow).isSolidTop())
				direction = direction.getOpposite();
		}

		entity.vel.x = direction.getXOffset() * GHOST_SPEED;
		entity.vel.y += 0.25f;
		entity.vel.y *= 0.8f;

		entity.move();

		C03EntityPositionPacket packet = new C03EntityPositionPacket(entity.pos.x,
				entity.pos.y, entity.identifier);
		((ServerMarniaWorld)entity.world).getSession().sendPacketToAll(packet);
	}
}

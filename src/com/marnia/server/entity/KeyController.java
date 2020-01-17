package com.marnia.server.entity;

import java.util.UUID;

import com.g4mesoft.math.MathUtils;
import com.marnia.client.net.packet.C03EntityPositionPacket;
import com.marnia.entity.Entity;
import com.marnia.entity.IController;
import com.marnia.entity.KeyEntity;
import com.marnia.server.world.ServerMarniaWorld;

public class KeyController implements IController {

	private static final float MIN_DIST = 1.0f;
	private static final float MOVE_FACTOR = 0.5f;
	
	@Override
	public void update(Entity entity) {
		Entity followEntity = getFollowEntity(entity);
		if (followEntity != null) {
			float cx = followEntity.getCenterX() - entity.getCenterX();
			float cy = followEntity.getCenterY() - entity.getCenterY();

			float dist = MathUtils.sqrt(cx * cx + cy * cy);
			cx /= dist;
			cy /= dist;
			
			float moveFactor = (dist - MIN_DIST) * MOVE_FACTOR;
			entity.vel.x = cx * moveFactor;
			entity.vel.y = cy * moveFactor;
			
			entity.move(false);
			
			C03EntityPositionPacket packet = new C03EntityPositionPacket(entity.pos.x,
					entity.pos.y, entity.identifier);
			((ServerMarniaWorld)entity.world).getSession().sendPacketToAll(packet);
		}
	}
	
	private Entity getFollowEntity(Entity entity) {
		UUID followIdentifier = ((KeyEntity)entity).getFollowIdentifier();
		return (followIdentifier == null) ? null : entity.world.getEntity(followIdentifier);
	}
}

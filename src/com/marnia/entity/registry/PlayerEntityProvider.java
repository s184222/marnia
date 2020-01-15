package com.marnia.entity.registry;

import java.io.IOException;
import java.util.UUID;

import com.marnia.entity.PlayerEntity;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.util.SpaceHelper;
import com.marnia.world.MarniaWorld;

public class PlayerEntityProvider implements IEntityProvider<PlayerEntity, EntityContainer> {

	@Override
	public EntityContainer decodeContainer(PacketDecoder packetDecoder) throws InterruptedException, IOException {
		packetDecoder.fetchData(SpaceHelper.FLOAT_MATCH, SpaceHelper.FLOAT_MATCH, SpaceHelper.UUID_MATCH);
		float x = packetDecoder.getData(Float.class, 0);
		float y = packetDecoder.getData(Float.class, 1);
		UUID identifier = packetDecoder.getData(UUID.class, 2);
		return new EntityContainer(x, y, identifier);
	}

	@Override
	public void encodeContainer(EntityContainer container, PacketEncoder packetEncoder) throws InterruptedException {
		packetEncoder.putData(container.getX(), container.getY(), container.getIdentifier());
	}

	@Override
	public EntityContainer getContainer(PlayerEntity entity) {
		return new EntityContainer(entity.pos.x, entity.pos.y, entity.identifier);
	}
	
	@Override
	public PlayerEntity getEntity(MarniaWorld world, EntityContainer container) {
		PlayerEntity playerEntity = new PlayerEntity(world, container.getIdentifier());
		playerEntity.moveToImmediately(container.getX(), container.getY());
		return playerEntity;
	}
	
	@Override
	public Class<PlayerEntity> getEntityClass() {
		return PlayerEntity.class;
	}
	
	@Override
	public Class<EntityContainer> getContainerClass() {
		return EntityContainer.class;
	}
}

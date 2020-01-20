package com.marnia.entity.registry;

import java.io.IOException;
import java.util.UUID;

import com.marnia.entity.Entity;
import com.marnia.entity.KeyEntity;
import com.marnia.entity.PlayerEntity;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.util.SpaceHelper;
import com.marnia.world.MarniaWorld;

public class KeyEntityProvider implements IEntityProvider<KeyEntity, EntityContainer> {

	private static final UUID NO_UUID = new UUID(0, 0);
	
	@Override
	public EntityContainer decodeContainer(PacketDecoder packetDecoder) throws InterruptedException, IOException {
		packetDecoder.fetchData(SpaceHelper.FLOAT_MATCH, SpaceHelper.FLOAT_MATCH, 
				SpaceHelper.UUID_MATCH, SpaceHelper.UUID_MATCH);

		float x = packetDecoder.getData(Float.class, 0);
		float y = packetDecoder.getData(Float.class, 1);
		UUID identifier = packetDecoder.getData(UUID.class, 2);
		UUID followingIdentifier = packetDecoder.getData(UUID.class, 3);
		
		return new KeyEntityContainer(x, y, identifier, followingIdentifier);
	}

	@Override
	public void encodeContainer(EntityContainer container, PacketEncoder packetEncoder) throws InterruptedException {
		UUID followingIdentifier = null;
		if (container instanceof KeyEntityContainer)
			followingIdentifier = ((KeyEntityContainer)container).getFollowingIdentifier();
		packetEncoder.putData(container.getX(), container.getY(), container.getIdentifier(), followingIdentifier);
	}

	@Override
	public EntityContainer getContainer(KeyEntity key) {
		UUID followingUUID = (key.getFollowIdentifier() == null) ? NO_UUID : key.getFollowIdentifier();
		return new KeyEntityContainer(key.pos.x, key.pos.y, key.getIdentifier(), followingUUID);
	}

	@Override
	public KeyEntity getEntity(MarniaWorld world, EntityContainer container, boolean placeAtFeet) {
		KeyEntity key = new KeyEntity(world);
		
		if (container instanceof KeyEntityContainer) {
			UUID followingIdentifier = ((KeyEntityContainer)container).getFollowingIdentifier();
			if (NO_UUID.equals(followingIdentifier)) {
				Entity playerEntity = world.getEntity(followingIdentifier);
				if (playerEntity instanceof PlayerEntity)
					key.setFollowing((PlayerEntity)playerEntity);
			}
		}
		
		key.setIdentifier(container.getIdentifier());
		key.moveToImmediately(container.getX(), container.getY(), placeAtFeet);
		
		return key;
	}

	@Override
	public Class<EntityContainer> getContainerClass() {
		return EntityContainer.class;
	}

	@Override
	public Class<KeyEntity> getEntityClass() {
		return KeyEntity.class;
	}
}

package com.marnia.entity.registry;

import java.io.IOException;
import java.util.UUID;

import com.marnia.entity.DoorEntity;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.util.SpaceHelper;
import com.marnia.world.MarniaWorld;

public class DoorEntityProvider implements IEntityProvider<DoorEntity, EntityContainer> {

	@Override
	public EntityContainer decodeContainer(PacketDecoder packetDecoder) throws InterruptedException, IOException {
		packetDecoder.fetchData(SpaceHelper.FLOAT_MATCH, SpaceHelper.FLOAT_MATCH, 
				SpaceHelper.UUID_MATCH, SpaceHelper.BOOLEAN_MATCH);
		
		float x = packetDecoder.getData(Float.class, 0);
		float y = packetDecoder.getData(Float.class, 1);
		UUID identifier = packetDecoder.getData(UUID.class, 2);
		boolean unlocked = packetDecoder.getData(Boolean.class, 3);
		
		return new DoorEntityContainer(x, y, identifier, unlocked);
	}

	@Override
	public void encodeContainer(EntityContainer container, PacketEncoder packetEncoder) throws InterruptedException {
		boolean unlocked = (container instanceof DoorEntityContainer) && ((DoorEntityContainer)container).isUnlocked();
		packetEncoder.putData(container.getX(), container.getY(), container.getIdentifier(), unlocked);
	}

	@Override
	public EntityContainer getContainer(DoorEntity door) {
		return new DoorEntityContainer(door.pos.x, door.pos.y, door.getIdentifier(), door.isUnlocked());
	}

	@Override
	public DoorEntity getEntity(MarniaWorld world, EntityContainer container, boolean placeAtFeet) {
		DoorEntity door = new DoorEntity(world);
		if (container instanceof DoorEntityContainer)
			door.setUnlocked(((DoorEntityContainer)container).isUnlocked());
		
		door.setIdentifier(container.getIdentifier());
		door.moveToImmediately(container.getX(), container.getY(), placeAtFeet);
		
		return door;
	}

	@Override
	public Class<EntityContainer> getContainerClass() {
		return EntityContainer.class;
	}

	@Override
	public Class<DoorEntity> getEntityClass() {
		return DoorEntity.class;
	}
}

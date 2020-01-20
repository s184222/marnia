package com.marnia.entity.registry;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;

import com.marnia.entity.Entity;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.util.SpaceHelper;
import com.marnia.world.MarniaWorld;

public abstract class BasicEntityProvider<E extends Entity> implements IEntityProvider<E, EntityContainer> {

	private final Function<MarniaWorld, E> constructor;
	
	public BasicEntityProvider(Function<MarniaWorld, E> constructor) {
		this.constructor = constructor;
	}
	
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
	public EntityContainer getContainer(E entity) {
		return new EntityContainer(entity.pos.x, entity.pos.y, entity.getIdentifier());
	}

	@Override
	public E getEntity(MarniaWorld world, EntityContainer container, boolean placeAtFeet) {
		E entity = constructor.apply(world);
		entity.setIdentifier(container.getIdentifier());
		entity.moveToImmediately(container.getX(), container.getY(), placeAtFeet);
		return entity;
	}
	
	@Override
	public Class<EntityContainer> getContainerClass() {
		return EntityContainer.class;
	}
}

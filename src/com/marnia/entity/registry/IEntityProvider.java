package com.marnia.entity.registry;

import java.io.IOException;

import com.marnia.entity.Entity;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.world.MarniaWorld;

public interface IEntityProvider<E extends Entity, C extends EntityContainer> {

	public C decodeContainer(PacketDecoder packetDecoder) throws InterruptedException, IOException;

	public void encodeContainer(C container, PacketEncoder packetEncoder) throws InterruptedException;
	
	public EntityContainer getContainer(E entity);

	public E getEntity(MarniaWorld world, C container);
	
	public Class<E> getEntityClass();

	public Class<C> getContainerClass();
	
}

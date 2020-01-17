package com.marnia.entity.registry;

import java.io.IOException;
import java.util.UUID;

import com.marnia.entity.PlayerColor;
import com.marnia.entity.PlayerEntity;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.util.SpaceHelper;
import com.marnia.world.MarniaWorld;

public class PlayerEntityProvider implements IEntityProvider<PlayerEntity, PlayerEntityContainer> {

	@Override
	public PlayerEntityContainer decodeContainer(PacketDecoder packetDecoder) throws InterruptedException, IOException {
		packetDecoder.fetchData(SpaceHelper.FLOAT_MATCH, SpaceHelper.FLOAT_MATCH, 
				SpaceHelper.UUID_MATCH, SpaceHelper.INTEGER_MATCH);
		
		float x = packetDecoder.getData(Float.class, 0);
		float y = packetDecoder.getData(Float.class, 1);
		UUID identifier = packetDecoder.getData(UUID.class, 2);
		PlayerColor color = PlayerColor.fromIndex(packetDecoder.getData(Integer.class, 3));
		
		return new PlayerEntityContainer(x, y, identifier, color);
	}

	@Override
	public void encodeContainer(PlayerEntityContainer container, PacketEncoder packetEncoder) throws InterruptedException {
		packetEncoder.putData(container.getX(), container.getY(), 
				container.getIdentifier(), container.getColor().getIndex());
	}

	@Override
	public PlayerEntityContainer getContainer(PlayerEntity player) {
		return new PlayerEntityContainer(player.pos.x, player.pos.y, player.identifier, player.getColor());
	}

	@Override
	public PlayerEntity getEntity(MarniaWorld world, PlayerEntityContainer container) {
		PlayerEntity player = new PlayerEntity(world, container.getIdentifier(), container.getColor());
		player.moveToImmediately(container.getX(), container.getY());
		return player;
	}

	@Override
	public Class<PlayerEntityContainer> getContainerClass() {
		return PlayerEntityContainer.class;
	}

	@Override
	public Class<PlayerEntity> getEntityClass() {
		return PlayerEntity.class;
	}
}

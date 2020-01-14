package com.marnia.client.net.packet;

import java.io.IOException;

import com.marnia.client.net.IClientNetworkHandler;
import com.marnia.net.packet.IPacket;
import com.marnia.net.packet.PacketDecoder;
import com.marnia.net.packet.PacketEncoder;
import com.marnia.util.SpaceHelper;
import com.marnia.world.WorldStorage;

public class C00WorldDataPacket implements IPacket<IClientNetworkHandler> {

	private int width;
	private int height;
	private int[] tiles;
	
	public C00WorldDataPacket() {
	}

	public C00WorldDataPacket(WorldStorage storage) {
		this(storage.getWidth(), storage.getHeight(), storage.getTiles());
	}
	
	public C00WorldDataPacket(int width, int height, int[] tiles) {
		this.width = width;
		this.height = height;
		this.tiles = tiles;
	}
	
	@Override
	public void decodePacket(PacketDecoder decoder) throws InterruptedException, IOException {
		decoder.fetchData(SpaceHelper.INT_MATCH, SpaceHelper.INT_MATCH, SpaceHelper.INT_ARRAY_MATCH);
		width = decoder.getData(Integer.class, 0);
		height = decoder.getData(Integer.class, 1);
		tiles = decoder.getData(int[].class, 2);
	}

	@Override
	public void encodePacket(PacketEncoder encoder) throws InterruptedException {
		encoder.putData(width, height, tiles);
	}

	@Override
	public void handlePacket(IClientNetworkHandler handler) {
		handler.onWorldDataPacket(this);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int[] getTiles() {
		return tiles;
	}
	
	public WorldStorage getStorage() {
		return new WorldStorage(width, height, tiles);
	}
}

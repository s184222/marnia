package com.marnia.world.gen;

import com.marnia.world.tile.Tile;

public class WorldStorage {

	private final int width;
	private final int height;

	private final int[] tiles;

	public WorldStorage(int width, int height) {
		this(width, height, new int[width * height]);

		for(int i = 0; i < tiles.length; i++)
			tiles[i] = Tile.AIR_TILE.getIndex();
	}

	public WorldStorage(int width, int height, int[] tiles) {
		this.width = width;
		this.height = height;
		this.tiles = tiles;
	}

	public boolean isInBounds(int xt, int yt) {
		return xt >= 0 && xt < width && yt >= 0 && yt < height;
	}

	public void setTileIndex(int xt, int yt, int tileIndex) {
		if (isInBounds(xt, yt))
			tiles[xt + yt * width] = tileIndex;
	}

	public int getTileIndex(int xt, int yt) {
		if (isInBounds(xt, yt))
			return tiles[xt + yt * width];
		return Tile.AIR_TILE.getIndex();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}

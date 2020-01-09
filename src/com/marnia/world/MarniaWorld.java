package com.marnia.world;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.math.MathUtils;
import com.marnia.world.tile.Tile;

public class MarniaWorld {

	public static final int WORLD_WIDTH = 100;
	public static final int WORLD_HEIGHT = 20;
	public static final int TILE_SIZE = 32;
	
	private int[] tiles;
	
	public MarniaWorld() {
		tiles = new int[WORLD_WIDTH * WORLD_HEIGHT];
	
		for (int xt = 0; xt < WORLD_WIDTH; xt++)
			setTile(xt, WORLD_HEIGHT - 1, Tile.SOLID_TILE);
	}
	
	public boolean isInBounds(int xt, int yt) {
		return xt >= 0 && xt < WORLD_WIDTH && yt >= 0 && yt < WORLD_HEIGHT;
	}
	
	public void setTile(int xt, int yt, Tile tile) {
		if (isInBounds(xt, yt))
			tiles[xt + yt * WORLD_WIDTH] = tile.getIndex();
	}

	public Tile getTile(int xt, int yt) {
		if (isInBounds(xt, yt))
			return Tile.getTile(tiles[xt + yt * WORLD_WIDTH]);
		return Tile.AIR_TILE;
	}
	
	public void tick() {
	}
	
	public void render(IRenderer2D renderer, float dt, DynamicCamera camera) {
		renderTiles(renderer, dt, camera);
	}
	
	private void renderTiles(IRenderer2D renderer, float dt, DynamicCamera camera) {
		float xOffset = camera.getXOffset(dt);
		float yOffset = camera.getYOffset(dt);
		
		int x0 = MathUtils.max(0, (int)xOffset);
		int y0 = MathUtils.max(0, (int)yOffset);
		int x1 = MathUtils.min(WORLD_WIDTH - 1, (int)(xOffset + camera.getViewWidth() + 0.5f));
		int y1 = MathUtils.min(WORLD_HEIGHT - 1, (int)(yOffset + camera.getViewHeight() + 0.5f));
	
		for (int xt = x0; xt <= x1; xt++) {
			for (int yt = y0; yt <= y1; yt++) {
				getTile(xt, yt).render(this, xt, yt, renderer, dt, camera);
			}
		}
	}
}

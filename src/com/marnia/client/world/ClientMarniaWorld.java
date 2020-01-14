package com.marnia.client.world;

import java.util.ArrayList;
import java.util.List;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.GColor;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.math.MathUtils;
import com.marnia.client.util.CameraUtil;
import com.marnia.client.world.entity.DrawableEntity;
import com.marnia.entity.Entity;
import com.marnia.world.MarniaWorld;
import com.marnia.world.WorldStorage;
import com.marnia.world.tile.Tile;

public class ClientMarniaWorld extends MarniaWorld {

	public static final int TILE_SIZE = 32;
	
	private byte[] spriteData;
	
	private List<DrawableEntity> drawableEntities;
	
	public ClientMarniaWorld() {
		spriteData = new byte[storage.getWidth() * storage.getHeight()];
	
		drawableEntities = new ArrayList<DrawableEntity>();
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	public void setTile(int xt, int yt, Tile tile) {
		super.setTile(xt, yt, tile);
	
		if (storage.isInBounds(xt, yt)) {
			byte data = getTile(xt, yt).getSpriteData(this, xt, yt);
			spriteData[xt + yt * getWidth()] = data;
		}
	}
	
	@Override
	public void setWorldStorage(WorldStorage storage) {
		super.setWorldStorage(storage);
		
		int numTiles = storage.getWidth() * storage.getHeight();
		if (spriteData.length != numTiles)
			spriteData = new byte[numTiles];
		
		int index = 0;
		for (int yt = 0; yt < storage.getHeight(); yt++)
			for (int xt = 0; xt < storage.getWidth(); xt++)
				spriteData[index++] = getTile(xt, yt).getSpriteData(this, xt, yt);
	}
	
	@Override
	public void addEntity(Entity entity) {
		super.addEntity(entity);
		
		if (entity instanceof DrawableEntity)
			drawableEntities.add((DrawableEntity)entity);
	}
	
	@Override
	public void removeEntity(Entity entity) {
		super.removeEntity(entity);
		
		if (entity instanceof DrawableEntity)
			drawableEntities.remove((DrawableEntity)entity);
	}
	
	public void render(IRenderer2D renderer, float dt, DynamicCamera camera) {
		renderTiles(renderer, dt, camera);
		
		for (DrawableEntity entity : drawableEntities)
			entity.render(renderer, dt, camera);
	}
	
	private void renderTiles(IRenderer2D renderer, float dt, DynamicCamera camera) {
		float xOffset = camera.getXOffset(dt);
		float yOffset = camera.getYOffset(dt);
		
		int x0 = MathUtils.max(0, (int)xOffset);
		int y0 = MathUtils.max(0, (int)yOffset);
		int x1 = MathUtils.min(getWidth() - 1, (int)(xOffset + camera.getViewWidth() + 0.5f));
		int y1 = MathUtils.min(getHeight() - 1, (int)(yOffset + camera.getViewHeight() + 0.5f));
	
		for (int xt = x0; xt <= x1; xt++) {
			for (int yt = y0; yt <= y1; yt++) {
				if (getTile(xt, yt) != Tile.AIR_TILE) {
					int xp = CameraUtil.getPixelX(xt, camera, dt);
					int yp = CameraUtil.getPixelY(yt, camera, dt);
					int w = CameraUtil.getPixelX(xt + 1, camera, dt) - xp;
					int h = CameraUtil.getPixelY(yt + 1, camera, dt) - yp;
					
					renderer.setColor(GColor.COLORS.get(getTile(xt, yt).getIndex() * 5));
					renderer.fillRect(xp, yp, w, h);
				}
			}
		}
	}
}

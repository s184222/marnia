package com.marnia.client.world;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.GColor;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.math.MathUtils;
import com.g4mesoft.world.phys.AABB;
import com.marnia.client.ClientMarniaApp;
import com.marnia.client.util.CameraUtil;
import com.marnia.entity.Entity;
import com.marnia.graphics.Texture;
import com.marnia.graphics.TileSheet;
import com.marnia.util.SpriteHelper;
import com.marnia.world.MarniaWorld;
import com.marnia.world.WorldStorage;
import com.marnia.world.tile.Tile;

public class ClientMarniaWorld extends MarniaWorld {

	public static final int TILE_SIZE = 32;
	
	private final ClientMarniaApp app;
	
	private int[] spriteData;
	
	public ClientMarniaWorld(ClientMarniaApp app) {
		this.app = app;
		
		spriteData = new int[storage.getWidth() * storage.getHeight()];
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	public void setTile(int xt, int yt, Tile tile) {
		super.setTile(xt, yt, tile);
	
		if (storage.isInBounds(xt, yt)) {
			int x1 = MathUtils.min(xt + 1, getWidth() - 1);
			int y1 = MathUtils.min(yt + 1, getHeight() - 1);
			for (int x = MathUtils.max(xt - 1, 0); x <= x1; x++) {
				for (int y = MathUtils.max(yt - 1, 0); y <= y1; y++) {
					int data = getTile(xt, yt).getSpriteData(this, x, y);
					spriteData[x + y * getWidth()] = data;
				}
			}
		}
	}
	
	@Override
	public void setWorldStorage(WorldStorage storage) {
		super.setWorldStorage(storage);
		
		int numTiles = storage.getWidth() * storage.getHeight();
		if (spriteData.length != numTiles)
			spriteData = new int[numTiles];
		
		int index = 0;
		for (int yt = 0; yt < storage.getHeight(); yt++)
			for (int xt = 0; xt < storage.getWidth(); xt++)
				spriteData[index++] = getTile(xt, yt).getSpriteData(this, xt, yt);
	}

	public int getSpriteData(int xt, int yt) {
		if (!storage.isInBounds(xt, yt))
			return SpriteHelper.getSpriteDataAt(0, 0);
		return spriteData[xt + yt * getWidth()];
	}

	public void render(IRenderer2D renderer, float dt, DynamicCamera camera) {
		renderBackground(renderer, dt, camera);
		renderEntities(renderer, dt, camera);
		renderTiles(renderer, dt, camera);
	}

	private void renderBackground(IRenderer2D renderer, float dt, DynamicCamera camera) {
		Texture background = app.getTextureLoader().getWorldBackground();

		int bgw = background.getWidth();
		int bgh = background.getHeight();

		int dw = renderer.getWidth();
		int dh = renderer.getHeight();

		if (bgw * dh > dw * bgh) {
			bgw = bgh * dw / bgw;
			bgh = dh;
		} else {
			bgh = bgw * dh / bgh;
			bgw = dw;
		}

		int bgx = (dw - bgw) / 2;
		int bgy = (dh - bgh) / 2;

		background.render(renderer, bgx, bgy, bgw, bgh);
	}

	private void renderEntities(IRenderer2D renderer, float dt, DynamicCamera camera) {
		for (Entity entity : entities) {
			float ix = entity.prevPos.x + (entity.pos.x - entity.prevPos.x) * dt;
			float iy = entity.prevPos.y + (entity.pos.y - entity.prevPos.y) * dt;

			AABB hitbox = entity.getHitbox();

			int xp = CameraUtil.getPixelX(ix, camera, dt);
			int yp = CameraUtil.getPixelY(iy, camera, dt);
			int w = CameraUtil.getPixelX(ix + hitbox.x1 - hitbox.x0, camera, dt) - xp;
			int h = CameraUtil.getPixelY(iy + hitbox.y1 - hitbox.y0, camera, dt) - yp;

			renderer.setColor(GColor.HOT_PINK);
			renderer.fillRect(xp, yp, w, h);
		}
	}
	
	private void renderTiles(IRenderer2D renderer, float dt, DynamicCamera camera) {
		float xOffset = camera.getXOffset(dt);
		float yOffset = camera.getYOffset(dt);

		float scale = camera.getScale(dt);
		float viewWidth = camera.getScreenWidth() / scale;
		float viewHeight = camera.getScreenHeight() / scale;

		int x0 = MathUtils.max(0, (int)xOffset);
		int y0 = MathUtils.max(0, (int)yOffset);
		int x1 = MathUtils.min(getWidth() - 1, (int)(xOffset + viewWidth + 0.5f));
		int y1 = MathUtils.min(getHeight() - 1, (int)(yOffset + viewHeight + 0.5f));

		TileSheet sheet = app.getTextureLoader().getWorldTileSheet();
		for (int xt = x0; xt <= x1; xt++) {
			for (int yt = y0; yt <= y1; yt++) {
				if (getTile(xt, yt) != Tile.AIR_TILE) {
					int xp = CameraUtil.getPixelX(xt, camera, dt);
					int yp = CameraUtil.getPixelY(yt, camera, dt);
					int w = CameraUtil.getPixelX(xt + 1, camera, dt) - xp;
					int h = CameraUtil.getPixelY(yt + 1, camera, dt) - yp;
					
					int spriteData = getSpriteData(xt, yt);
					int sx = SpriteHelper.getSpriteX(spriteData);
					int sy = SpriteHelper.getSpriteY(spriteData);

					sheet.render(renderer, xp, yp, w, h, sx, sy);
				}
			}
		}
	}
	
	public ClientMarniaApp getMarniaApp() {
		return app;
	}

	@Override
	public boolean isServer() {
		return false;
	}
}

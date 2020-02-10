package com.marnia.client.world;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.math.MathUtils;
import com.marnia.client.ClientMarniaApp;
import com.marnia.client.entity.model.DoorEntityModel;
import com.marnia.client.entity.model.EntityModel;
import com.marnia.client.entity.model.EntityModelRegistry;
import com.marnia.client.entity.model.GhostEntityModel;
import com.marnia.client.entity.model.KeyEntityModel;
import com.marnia.client.entity.model.PlayerEntityModel;
import com.marnia.client.util.CameraUtil;
import com.marnia.decorations.Decoration;
import com.marnia.decorations.DecorationType;
import com.marnia.entity.DoorEntity;
import com.marnia.entity.Entity;
import com.marnia.entity.GhostEntity;
import com.marnia.entity.KeyEntity;
import com.marnia.entity.PlayerEntity;
import com.marnia.graphics.Texture;
import com.marnia.graphics.TextureTheme;
import com.marnia.graphics.TileSheet;
import com.marnia.util.SpriteHelper;
import com.marnia.world.MarniaWorld;
import com.marnia.world.WorldStorage;
import com.marnia.world.WorldTheme;
import com.marnia.world.tile.Tile;

public class ClientMarniaWorld extends MarniaWorld {

	private static final float MAX_VIEW_ABOVE = 10.0f;
	private static final float LAYER_PARALLAXING_FACTOR = 0.025f;
	
	private final ClientMarniaApp app;
	
	private int[] spriteData;

	private final EntityModelRegistry entityModelRegistry;
	private final Map<UUID, EntityModel<?>> entityModels;
	
	private ParallaxedWorldTexture[] backgroundLayers;
	
	public ClientMarniaWorld(ClientMarniaApp app) {
		super(WorldTheme.PLAINS);
		
		this.app = app;
		
		spriteData = new int[storage.getWidth() * storage.getHeight()];

		entityModelRegistry = new EntityModelRegistry();
		entityModels = new LinkedHashMap<UUID, EntityModel<?>>();

		backgroundLayers = new ParallaxedWorldTexture[0];
		
		registerEntityModels();
		initParallaxing();
	}

	private void registerEntityModels() {
		entityModelRegistry.registerModelProvider(PlayerEntity.class, PlayerEntityModel::new);
		entityModelRegistry.registerModelProvider(GhostEntity.class, GhostEntityModel::new);
		entityModelRegistry.registerModelProvider(KeyEntity.class, KeyEntityModel::new);
		entityModelRegistry.registerModelProvider(DoorEntity.class, DoorEntityModel::new);
	}
	
	public void tickEntityModels() {
		for (EntityModel<?> entityModel : entityModels.values())
			entityModel.tick();
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
		
		app.getCamera().setBounds(0.0f, -MAX_VIEW_ABOVE, getWidth(), getHeight());
	}

	public int getSpriteData(int xt, int yt) {
		if (!storage.isInBounds(xt, yt))
			return SpriteHelper.getSpriteDataAt(0, 0);
		return spriteData[xt + yt * getWidth()];
	}

	@Override
	public void addEntity(Entity entity) {
		super.addEntity(entity);

		entityModels.put(entity.getIdentifier(), entityModelRegistry.getEntityModel(entity));
	}

	@Override
	public boolean removeEntity(Entity entity) {
		if (super.removeEntity(entity)) {
			entityModels.remove(entity.getIdentifier());
			return true;
		}
		
		return false;
	}

	public void render(IRenderer2D renderer, float dt, DynamicCamera camera) {
		renderBackground(renderer, dt, camera);
		renderDecorations(renderer, dt, camera);
		renderEntities(renderer, dt, camera);
		renderTiles(renderer, dt, camera);
	}
	
	private void renderBackground(IRenderer2D renderer, float dt, DynamicCamera camera) {
		// The camera can go above the world, so the parallaxed
		// background might not be covering the entirety of the
		// visible viewport. Render solid background color here.
		if (camera.getYOffset() < 0.0f) {
			renderer.setColor(getTheme().getSkyColor());
			renderer.clear();
		}

		for (ParallaxedWorldTexture layer : backgroundLayers)
			layer.render(renderer, dt, camera);
	}

	public void renderDecorations(IRenderer2D renderer, float dt, DynamicCamera camera) {
		TextureTheme textureTheme = getTextureTheme();
		int dw = renderer.getWidth();
		int dh = renderer.getHeight();
		
		for (Decoration decoration : decorations) {
			DecorationType type = decoration.getType();
			Texture texture = textureTheme.getDecorationTexture(type);
			
			int h = CameraUtil.getScaledSize(type.getHeight(), camera, dt);
			int yp = CameraUtil.getPixelY(decoration.getY(), camera, dt) - h;
			
			int w = texture.getWidth() * h / texture.getHeight();
			int xp = CameraUtil.getPixelX(decoration.getX(), camera, dt) - w / 2;
		
			if (yp + h >= 0 && yp < dh && xp + w >= 0 && xp < dw)
				texture.render(renderer, xp, yp, w, h);
		}
	}
	
	private void renderEntities(IRenderer2D renderer, float dt, DynamicCamera camera) {
		for (EntityModel<?> entityModel : entityModels.values()) {
			if (entityModel.isInBounds(camera, dt))
				entityModel.render(renderer, dt, camera);
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

		TileSheet sheet = getTextureTheme().getWorldTileSheet();
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
	
	private void initParallaxing() {
		Texture[] backgrounds = getTextureTheme().getWorldBackgrounds();
		backgroundLayers = new ParallaxedWorldTexture[backgrounds.length];
		for (int i = 0; i < backgrounds.length; i++) {
			float parallaxing = LAYER_PARALLAXING_FACTOR * (i + 1);
			backgroundLayers[i] = new ParallaxedWorldTexture(this, backgrounds[i], parallaxing);
		}
		
	}
	
	@Override
	public void setTheme(WorldTheme theme) {
		super.setTheme(theme);
		
		initParallaxing();
		
		for (EntityModel<?> entityModel : entityModels.values())
			entityModel.onThemeChanged(theme);
	}
	
	public TextureTheme getTextureTheme() {
		return app.getTextureLoader().getTheme(getTheme());
	}

	@Override
	public void clearWorld() {
		super.clearWorld();
		
		entityModels.clear();
	}
	
	public ClientMarniaApp getMarniaApp() {
		return app;
	}

	@Override
	public boolean isServer() {
		return false;
	}
}

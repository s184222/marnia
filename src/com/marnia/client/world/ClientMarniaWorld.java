package com.marnia.client.world;

import java.awt.event.KeyEvent;

import com.g4mesoft.Application;
import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.input.key.KeyInput;
import com.g4mesoft.input.key.KeySingleInput;
import com.g4mesoft.math.MathUtils;
import com.marnia.client.world.entity.ClientController;
import com.marnia.client.world.entity.ClientPlayer;
import com.marnia.world.MarniaWorld;
import com.marnia.world.WorldStorage;

public class ClientMarniaWorld extends MarniaWorld {

	public static final int TILE_SIZE = 32;
	
	private ClientPlayer player;
	
	private byte[] spriteData;
	
	public ClientMarniaWorld() {
		KeyInput left = new KeySingleInput("left", KeyEvent.VK_A, KeyEvent.VK_LEFT);
		KeyInput right = new KeySingleInput("right", KeyEvent.VK_D, KeyEvent.VK_RIGHT);
		KeyInput jump = new KeySingleInput("jump", KeyEvent.VK_W, KeyEvent.VK_SPACE, KeyEvent.VK_UP);
		Application.addKeys(left, right, jump);
		
		player = new ClientPlayer(this, new ClientController(left, right, jump));
	
		spriteData = new byte[storage.getWidth() * storage.getHeight()];
	}
	
	@Override
	public void tick() {
		super.tick();
		
		player.tick();
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
				spriteData[index] = getTile(xt, yt).getSpriteData(this, xt, yt);
	}
	
	public void render(IRenderer2D renderer, float dt, DynamicCamera camera) {
		renderTiles(renderer, dt, camera);
		
		player.render(renderer, dt, camera);
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
				// TODO: render stuff..
			}
		}
	}
	
	public ClientPlayer getPlayer(){
		return player;
	}
}

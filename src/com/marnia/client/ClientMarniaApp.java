package com.marnia.client;

import java.io.IOException;

import org.jspace.RemoteSpace;

import com.g4mesoft.Application;
import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.Display;
import com.g4mesoft.graphic.DisplayConfig;
import com.g4mesoft.graphic.DisplayMode;
import com.g4mesoft.graphic.GColor;
import com.g4mesoft.graphic.IRenderer2D;
import com.marnia.MarniaApp;
import com.marnia.client.menu.ConnectMenu;
import com.marnia.client.world.ClientMarniaWorld;
import com.marnia.client.world.entity.ClientPlayer;
import com.marnia.world.MarniaWorld;

public class ClientMarniaApp extends MarniaApp {

	private static final String TITLE = "Marnia App";
	private static final String ICON_PATH = "/icon.png";

	private DynamicCamera camera;
	
	public ClientMarniaApp() {
		super(new DisplayConfig(TITLE, 720, 454, 100, 100, 
				                true, true, DisplayMode.NORMAL, 
				                true, ICON_PATH));
	}
	
	@Override
	public void init() {
		super.init();

		lobbySpace = null;
		gameplaySpace = null;
		
		camera = new DynamicCamera();
		
		// Ensure that camera does not move out of bounds.
		camera.setBounds(0.0f, 0.0f, MarniaWorld.WORLD_WIDTH, MarniaWorld.WORLD_HEIGHT);
		
		setRootComposition(new ConnectMenu(this));
	}
	
	public void connectToServer(String address, String port) throws IOException {
		lobbySpace = new RemoteSpace(getGateAddress(address, port, LOBBY_SPACE_NAME));
		gameplaySpace = new RemoteSpace(getGateAddress(address, port, GAMEPLAY_SPACE_NAME));
	
		// TODO: send some connection packets.
	}
	
	@Override
	protected MarniaWorld initWorlds() {
		return new ClientMarniaWorld();
	}
	
	@Override
	public void tick() {
		updateCamera();

		super.tick();
	}
	
	private void updateCamera() {
		camera.update();

		Display display = getDisplay();
		float dtw = (float)display.getWidth() / ClientMarniaWorld.TILE_SIZE;
		float dth = (float)display.getHeight() / ClientMarniaWorld.TILE_SIZE;

		camera.setScreenSize(dtw, dth);

        ClientPlayer player = ((ClientMarniaWorld)world).getPlayer();
        camera.setCenterX((camera.getCenterX() + player.getCenterX()) * 0.5f);
        camera.setCenterY((camera.getCenterY() + player.getCenterY()) * 0.5f);
	}
	
	@Override
	protected void render(IRenderer2D renderer, float dt) {
		renderer.setColor(GColor.WHITE);
		renderer.clear();
	
		((ClientMarniaWorld)world).render(renderer, dt, camera);
	}
	
	public static void main(String[] args) throws Exception {
		Application.start(args, ClientMarniaApp.class);
	}
}

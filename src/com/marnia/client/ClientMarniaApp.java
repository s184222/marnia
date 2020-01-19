package com.marnia.client;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.UUID;

import org.jspace.RemoteSpace;
import org.jspace.Space;

import com.g4mesoft.Application;
import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.Display;
import com.g4mesoft.graphic.DisplayConfig;
import com.g4mesoft.graphic.DisplayMode;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.input.key.KeyInput;
import com.g4mesoft.input.key.KeySingleInput;
import com.marnia.MarniaApp;
import com.marnia.client.entity.ClientController;
import com.marnia.client.menu.LobbyMenu;
import com.marnia.client.menu.MainMarniaMenu;
import com.marnia.client.net.ClientGameplayNetworkManager;
import com.marnia.client.net.ClientLobbyArea;
import com.marnia.client.world.ClientMarniaWorld;
import com.marnia.entity.IController;
import com.marnia.entity.PlayerEntity;
import com.marnia.graphics.TextureLoader;
import com.marnia.net.ILobbyEventListener;

public class ClientMarniaApp extends MarniaApp implements ILobbyEventListener {

	private static final String TITLE = "Marnia App";
	private static final String ICON_PATH = "/icon.png";

	private static final float MAX_VIEW_WIDTH = 25.0f;

	private static final float CAMERA_SCALE_EASING_FACTOR = 0.2f;
	
	private ClientLobbyArea lobbyArea;
	private LobbyMenu lobbyMenu;
	private boolean connected;
	
	private UUID serverIdentifier;
	private UUID identifier;
	private ClientGameplayNetworkManager networkManager;
	
	private ClientMarniaWorld world;
	private DynamicCamera camera;

	private float cameraScaleFactor;
	private float cameraScaleFactorTarget;

	private PlayerEntity player;
	
	private KeyInput leftKey;
	private KeyInput rightKey;
	private KeyInput jumpKey;
	private KeyInput fullscreenKey;

	private TextureLoader textureLoader;
	
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
		
		lobbyArea = null;
		lobbyMenu = null;

		camera = new DynamicCamera();
		camera.setZoomToCenter(true);
		cameraScaleFactor = 1.0f;
		cameraScaleFactorTarget = 1.0f;
		
		// Ensure that camera does not move out of bounds.
		camera.setZoomToCenter(true);
		
		leftKey = new KeySingleInput("left", KeyEvent.VK_A, KeyEvent.VK_LEFT);
		rightKey = new KeySingleInput("right", KeyEvent.VK_D, KeyEvent.VK_RIGHT);
		jumpKey = new KeySingleInput("jump", KeyEvent.VK_W, KeyEvent.VK_SPACE, KeyEvent.VK_UP);
		fullscreenKey = new KeySingleInput("fullscreen", KeyEvent.VK_F11);

		Application.addKeys(leftKey, rightKey, jumpKey, fullscreenKey);

		textureLoader = new TextureLoader();

		try {
			textureLoader.loadTextures();
		} catch (IOException e) {
			e.printStackTrace();
			stopRunning();
			return;
		}

		setMinimumFps(120.0);

		setRootComposition(new MainMarniaMenu(this));
	}
	
	@Override
	protected void stop() {
		super.stop();
	
		disconnectFromServer();
	}
	
	public boolean connectToServer(String username, String address, String port) {
		connected = false;

		try {	
			lobbySpace = new RemoteSpace(getGateAddress(address, port, LOBBY_SPACE_NAME));
			gameplaySpace = new RemoteSpace(getGateAddress(address, port, GAMEPLAY_SPACE_NAME));
		} catch (IOException e) {
			return false;
		}

		lobbyArea = new ClientLobbyArea(lobbySpace);
		lobbyArea.addEventListener(this);
		lobbyArea.setUsername(username);
		lobbyArea.start();
		
		return true;
	}
	
	public void disconnectFromServer() {
		if (lobbyArea != null && lobbyArea.isRunning()) {
			lobbyArea.stop();
			lobbyArea = null;
		}
		
		if (networkManager != null && networkManager.isRunning()) {
			networkManager.stop();
			networkManager = null;
		}

		identifier = serverIdentifier = null;
		
		closeRemoteSpace(lobbySpace);
		closeRemoteSpace(gameplaySpace);
		
		lobbySpace = gameplaySpace = null;
		
		connected = false;
	}
	
	private void closeRemoteSpace(Space remoteSpace) {
		if (remoteSpace != null) {
			try {
				((RemoteSpace)remoteSpace).close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void tick() {
		cameraScaleFactor += (cameraScaleFactorTarget - cameraScaleFactor) * CAMERA_SCALE_EASING_FACTOR;

		if (world != null) {
			updateCamera();
			
			world.tick();
			networkManager.tick();

			// This must be done after updating the network manager.
			world.tickEntityModels();
		}

		if (lobbyArea != null)
			lobbyArea.tick();
		
		if (fullscreenKey.isClicked()) {
			Display display = getDisplay();
			if (display.isFullscreen()) {
				display.setDisplayMode(DisplayMode.NORMAL);
			} else {
				display.setDisplayMode(DisplayMode.FULLSCREEN_BORDERLESS);
			}
			
			fullscreenKey.reset();
		}
	}
	
	private void updateCamera() {
		camera.update();

		Display display = getDisplay();
		camera.setScreenSize(display.getWidth(), display.getHeight());
		camera.setScale(display.getWidth() / MAX_VIEW_WIDTH * cameraScaleFactor);
	}
	
	@Override
	protected void render(IRenderer2D renderer, float dt) {
		if (world != null)
			world.render(renderer, dt, camera);
	}
	
	@Override
	public void onLobbyEvent(int eventId) {
		switch (eventId) {
		case ClientLobbyArea.LOBBY_CLOSED_EVENT:
			if (connected) {
				closeLobby();
			} else {
				// We have to reconnect.
				disconnectFromServer();
			}
			break;
		case ClientLobbyArea.CONNECTION_SUCCESSFUL_EVENT:
			connected = true;
			lobbyMenu = new LobbyMenu(this);
			setRootComposition(lobbyMenu);
			break;
		case ClientLobbyArea.PLAYER_ADDED_EVENT:
			if (lobbyMenu != null)
				lobbyMenu.setNames(lobbyArea.getPlayers());
			break;
		case ClientLobbyArea.GAME_STARTING_EVENT:
			initGameplayNetwork(lobbyArea.getServerIdentifier(), lobbyArea.getClientIdentifier());
			break;
		}
	}
	
	private void closeLobby() {
		lobbyArea.stop();
		lobbyArea = null;
		lobbyMenu = null;
		
		setRootComposition(null);
	}
	
	private void initGameplayNetwork(UUID serverIdentifier, UUID identifier) {
		if (serverIdentifier == null || identifier == null)
			throw new IllegalArgumentException("An identifier was null.");
		
		this.serverIdentifier = serverIdentifier;
		this.identifier = identifier;
		
		networkManager = new ClientGameplayNetworkManager(this, serverIdentifier, gameplaySpace, identifier, registry);
		networkManager.start();
		
		world = new ClientMarniaWorld(this);
	
		player = null;
	}

	public void setPlayerEntity(PlayerEntity player) {
		this.player = player;
	}
	
	public PlayerEntity getPlayer() {
		return player;
	}

	public void setCameraScaleFactorTarget(float scaleFactorTarget) {
		cameraScaleFactorTarget = scaleFactorTarget;
	}
	
	public UUID getServerIdentifier() {
		return serverIdentifier;
	}
	
	public UUID getIdentifier() {
		return identifier;
	}

	public ClientMarniaWorld getWorld() {
		return world;
	}
	
	public ClientGameplayNetworkManager getNetworkManager() {
		return networkManager;
	}
	
	public DynamicCamera getCamera() {
		return camera;
	}
	
	public IController getClientController() {
		return new ClientController(leftKey, rightKey, jumpKey);
	}

	public TextureLoader getTextureLoader() {
		return textureLoader;
	}
	
	public static void main(String[] args) throws Exception {
		Application.start(args, ClientMarniaApp.class);
	}
}

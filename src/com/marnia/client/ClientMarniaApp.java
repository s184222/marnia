package com.marnia.client;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jspace.RemoteSpace;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import com.g4mesoft.Application;
import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.Display;
import com.g4mesoft.graphic.DisplayConfig;
import com.g4mesoft.graphic.DisplayMode;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.input.key.KeyInput;
import com.g4mesoft.input.key.KeySingleInput;
import com.g4mesoft.sound.processor.AudioSource;
import com.marnia.MarniaApp;
import com.marnia.client.entity.ClientController;
import com.marnia.client.menu.LobbyMarniaMenu;
import com.marnia.client.menu.MainMarniaMenu;
import com.marnia.client.net.ClientGameplayNetworkManager;
import com.marnia.client.net.ClientLobbyArea;
import com.marnia.client.sound.SoundLoader;
import com.marnia.client.world.ClientMarniaWorld;
import com.marnia.entity.IController;
import com.marnia.entity.PlayerColor;
import com.marnia.entity.PlayerEntity;
import com.marnia.graphics.TextureLoader;
import com.marnia.net.ILobbyEventListener;
import com.marnia.server.GameplayProfile;
import com.marnia.server.GameplaySession;
import com.marnia.server.net.ServerGameplayNetworkManager;

public class ClientMarniaApp extends MarniaApp implements ILobbyEventListener {

	private static final String TITLE = "Marnia App";
	private static final String ICON_PATH = "/icon.png";

	private static final float MAX_VIEW_WIDTH = 20.0f;

	private static final float CAMERA_SCALE_EASING_FACTOR = 0.2f;
	
	private ClientLobbyArea lobbyArea;
	private LobbyMarniaMenu lobbyMenu;
	private boolean connected;
	
	private UUID serverIdentifier;
	private UUID identifier;
	private ClientGameplayNetworkManager networkManager;
	
	private ClientMarniaWorld world;
	private DynamicCamera camera;

	private float cameraScaleFactor;
	private float cameraScaleFactorTarget;

	private PlayerEntity player;
	private GameplaySession practiceSession;
	private ServerGameplayNetworkManager practiceNetworkManager;
	
	private KeyInput leftKey;
	private KeyInput rightKey;
	private KeyInput jumpKey;
	private KeyInput openDoorKey;
	
	private KeyInput fullscreenKey;

	private TextureLoader textureLoader;
	private AudioSource ambience;
	
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
		openDoorKey = new KeySingleInput("openDoor", KeyEvent.VK_S, KeyEvent.VK_DOWN);
		
		fullscreenKey = new KeySingleInput("fullscreen", KeyEvent.VK_F11);

		Application.addKeys(leftKey, rightKey, jumpKey, openDoorKey, fullscreenKey);

		textureLoader = new TextureLoader();

		try {
			textureLoader.loadTextures();
			SoundLoader.loadSounds();
		} catch (IOException e) {
			e.printStackTrace();
			stopRunning();
			return;
		}

		setMinimumFps(60.0);

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
	
	public void connectToPractice() {
		if (lobbyArea != null || networkManager != null)
			disconnectFromServer();

		UUID identifier = UUID.randomUUID();
		UUID practiceServerIdentifier;
		do {
			practiceServerIdentifier = UUID.randomUUID();
		} while (practiceServerIdentifier.equals(identifier));
		
		gameplaySpace = new SequentialSpace();
		practiceNetworkManager = new ServerGameplayNetworkManager(gameplaySpace, practiceServerIdentifier, registry);
		practiceNetworkManager.start();
		
		List<GameplayProfile> profiles = new ArrayList<GameplayProfile>();
		profiles.add(new GameplayProfile("Practice", identifier, PlayerColor.RED));
		practiceSession = new GameplaySession(practiceNetworkManager, profiles);
		practiceNetworkManager.addPlayer(identifier, practiceSession);
		
		initGameplayNetwork(practiceServerIdentifier, identifier);
	
		setRootComposition(null);
	
		practiceSession.startGame();
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
		
		if (practiceNetworkManager != null) {
			practiceNetworkManager.stop();
			practiceNetworkManager = null;
			practiceSession = null;
		} else {
			closeRemoteSpace(lobbySpace);
			closeRemoteSpace(gameplaySpace);
		}
		
		lobbySpace = gameplaySpace = null;
		
		if (ambience != null) {
			ambience.stop();
			ambience = null;
		}
		
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
		if (practiceNetworkManager != null)
			practiceNetworkManager.tick();
		if (practiceSession != null)
			practiceSession.tick();
		
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
		camera.setScale(getDefaultCameraScale() * cameraScaleFactor);
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
			lobbyMenu = new LobbyMarniaMenu(this);
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

		ambience = SoundLoader.playForever(SoundLoader.BACKGROUND_SOUND_ID, 1.0f);
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
	
	public float getDefaultCameraScale() {
		return getDisplay().getWidth() / MAX_VIEW_WIDTH;
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
		return new ClientController(leftKey, rightKey, jumpKey, openDoorKey);
	}

	public TextureLoader getTextureLoader() {
		return textureLoader;
	}
	
	public static void main(String[] args) throws Exception {
		Application.start(args, ClientMarniaApp.class);
	}
}

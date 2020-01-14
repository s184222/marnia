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
import com.g4mesoft.graphic.GColor;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.input.key.KeyInput;
import com.g4mesoft.input.key.KeySingleInput;
import com.marnia.MarniaApp;
import com.marnia.client.menu.ConnectMenu;
import com.marnia.client.menu.LobbyMenu;
import com.marnia.client.net.ClientGameplayNetworkManager;
import com.marnia.client.net.ClientLobbyArea;
import com.marnia.client.world.ClientMarniaWorld;
import com.marnia.client.world.entity.ClientController;
import com.marnia.client.world.entity.ClientPlayer;
import com.marnia.net.ILobbyEventListener;
import com.marnia.server.net.packet.S02PlayerPositionPacket;

public class ClientMarniaApp extends MarniaApp implements ILobbyEventListener {

	private static final String TITLE = "Marnia App";
	private static final String ICON_PATH = "/icon.png";

	private static final float MAX_VIEW_WIDTH = 25.0f;
	private static final float MAX_VIEW_ABOVE = 10.0f;
	
	private ClientLobbyArea lobbyArea;
	private LobbyMenu lobbyMenu;
	private boolean connected;
	
	private UUID serverIdentifier;
	private UUID identifier;
	private ClientGameplayNetworkManager networkManager;
	
	private ClientMarniaWorld world;
	private ClientPlayer player;
	private DynamicCamera camera;	
	
	private KeyInput leftKey;
	private KeyInput rightKey;
	private KeyInput jumpKey;
	private KeyInput fullscreenKey;
	
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
		
		// Ensure that camera does not move out of bounds.
		camera.setZoomToCenter(true);
		
		leftKey = new KeySingleInput("left", KeyEvent.VK_A, KeyEvent.VK_LEFT);
		rightKey = new KeySingleInput("right", KeyEvent.VK_D, KeyEvent.VK_RIGHT);
		jumpKey = new KeySingleInput("jump", KeyEvent.VK_W, KeyEvent.VK_SPACE, KeyEvent.VK_UP);
		fullscreenKey = new KeySingleInput("fullscreen", KeyEvent.VK_F11);

		Application.addKeys(leftKey, rightKey, jumpKey, fullscreenKey);
		
		setMinimumFps(120.0);

		setRootComposition(new ConnectMenu(this));
	}
	
	@Override
	protected void stop() {
		super.stop();
	
		disconnectFromServer();
	}
	
	public boolean connectToServer(String address, String port, String username) {
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
	
	private int timer = 0;
	
	@Override
	public void tick() {
		if (world != null) {
			updateCamera();
			
			world.tick();
			
			if (timer++ > 4) {
				timer = 0;
				networkManager.sendPacket(new S02PlayerPositionPacket(player.pos.x, player.pos.y), serverIdentifier);
			}
		}
		
		if (lobbyArea != null)
			lobbyArea.tick();
		if (networkManager != null)
			networkManager.tick();
		
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
		float dtw = (float)display.getWidth() / ClientMarniaWorld.TILE_SIZE;
		float dth = (float)display.getHeight() / ClientMarniaWorld.TILE_SIZE;

		camera.setScreenSize(dtw, dth);
		camera.setScale(dtw / MAX_VIEW_WIDTH);

		// TODO: listeners for this.
		camera.setBounds(0.0f, -MAX_VIEW_ABOVE, world.getWidth(), world.getHeight());

        camera.setCenterX((camera.getCenterX() + player.getCenterX()) * 0.5f);
        camera.setCenterY((camera.getCenterY() + player.getCenterY()) * 0.5f);
	}
	
	@Override
	protected void render(IRenderer2D renderer, float dt) {
		renderer.setColor(GColor.WHITE);
		renderer.clear();
	
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
			lobbyMenu = new LobbyMenu();
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
		
		world = new ClientMarniaWorld();
		player = new ClientPlayer(world, identifier, new ClientController(leftKey, rightKey, jumpKey));
		world.addEntity(player);
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
	
	public static void main(String[] args) throws Exception {
		Application.start(args, ClientMarniaApp.class);
	}
}

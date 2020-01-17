package com.marnia.graphics;

import javax.imageio.ImageIO;
import java.io.IOException;

public class TextureLoader {

	private static final String WORLD_SHEET_PATH = "/textures/normal/tilesheet.png";
	private static final int WORLD_SHEET_TW = 128;
	private static final int WORLD_SHEET_TH = 128;

	private static final String WORLD_BACKGROUND_PATH = "/textures/normal/background.png";

	private static final String PLAYER_IDLE_SHEET_PATH = "/textures/player/idle.png";
	private static final int PLAYER_IDLE_SHEET_TW = 144;
	private static final int PLAYER_IDLE_SHEET_TH = 155;

	private static final String PLAYER_JUMP_SHEET_PATH = "/textures/player/jump.png";
	private static final int PLAYER_JUMP_SHEET_TW = 151;
	private static final int PLAYER_JUMP_SHEET_TH = 155;

	private static final String PLAYER_RUN_SHEET_PATH = "/textures/player/run.png";
	private static final int PLAYER_RUN_SHEET_TW = 200;
	private static final int PLAYER_RUN_SHEET_TH = 155;

	private static final String GHOST_SHEET = "/textures/ghost_normal.png";
	private static final int GHOST_TW = 51;
	private static final int GHOST_TH = 73;

	private static final String KEY_SHEET = "/textures/key.png";
	private static final int KEY_TW = 0;
	private final int KEY_TH = 0;



	private TileSheet worldTileSheet;
	private Texture worldBackground;

	private TileSheet playerIdleTileSheet;
	private TileSheet playerJumpTileSheet;
	private TileSheet playerRunTileSheet;
	private TileSheet ghostTileSheet;
	private TileSheet keyTileSheet;

	public TextureLoader() {
	}

	public void loadTextures() throws IOException {
		worldTileSheet = readTileSheet(WORLD_SHEET_PATH, WORLD_SHEET_TW, WORLD_SHEET_TH);
		worldBackground = readTexture(WORLD_BACKGROUND_PATH);

		playerIdleTileSheet = readTileSheet(PLAYER_IDLE_SHEET_PATH, PLAYER_IDLE_SHEET_TW, PLAYER_IDLE_SHEET_TH);
		playerJumpTileSheet = readTileSheet(PLAYER_JUMP_SHEET_PATH, PLAYER_JUMP_SHEET_TW, PLAYER_JUMP_SHEET_TH);
		playerRunTileSheet = readTileSheet(PLAYER_RUN_SHEET_PATH, PLAYER_RUN_SHEET_TW, PLAYER_RUN_SHEET_TH);
		ghostTileSheet = readTileSheet(GHOST_SHEET,GHOST_TW, GHOST_TH);
		keyTileSheet= readTileSheet(KEY_SHEET, KEY_TW, KEY_TH);

	}

	private TileSheet readTileSheet(String path, int tileWidth, int tileHeight) throws IOException {
		return new TileSheet(readTexture(path), tileWidth, tileHeight);
	}

	private Texture readTexture(String path) throws IOException {
		return new Texture(ImageIO.read(TextureLoader.class.getResource(path)));
	}

	public TileSheet getWorldTileSheet() {
		return worldTileSheet;
	}

	public Texture getWorldBackground() {
		return worldBackground;
	}

	public TileSheet getPlayerIdleTileSheet() {
		return playerIdleTileSheet;
	}

	public TileSheet getPlayerJumpTileSheet() {
		return playerJumpTileSheet;
	}

	public TileSheet getPlayerRunTileSheet() {
		return playerRunTileSheet;
	}

	public TileSheet getGhostTileSheet() { return ghostTileSheet; }

	public TileSheet getKeyTileSheet() { return keyTileSheet; }
}

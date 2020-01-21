package com.marnia.graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.g4mesoft.graphic.filter.FastGaussianBlurPixelFilter;
import com.marnia.entity.PlayerColor;

public class TextureLoader {

	private static final String WORLD_SHEET_PATH = "/textures/desert/tilesheet.png";
	private static final int WORLD_SHEET_TW = 128;
	private static final int WORLD_SHEET_TH = 128;

	private static final String COMPOSED_WORLD_BACKGROUND_PATH = "/textures/desert/background.png";
	private static final String WORLD_BACKGROUNDS_PATH = "/textures/desert/background_%d.png";
	private static final int NUM_WORLD_BACKGROUNDS = 6;
	
	private static final String PLAYER_IDLE_SHEET_PATH = "/textures/player/idle_%s.png";
	private static final int PLAYER_IDLE_SHEET_TW = 144;
	private static final int PLAYER_IDLE_SHEET_TH = 155;

	private static final String PLAYER_BLINK_SHEET_PATH = "/textures/player/blink_%s.png";
	private static final int PLAYER_BLINK_SHEET_TW = 144;
	private static final int PLAYER_BLINK_SHEET_TH = 155;

	private static final String PLAYER_JUMP_SHEET_PATH = "/textures/player/jump_%s.png";
	private static final int PLAYER_JUMP_SHEET_TW = 151;
	private static final int PLAYER_JUMP_SHEET_TH = 155;

	private static final String PLAYER_RUN_SHEET_PATH = "/textures/player/run_%s.png";
	private static final int PLAYER_RUN_SHEET_TW = 200;
	private static final int PLAYER_RUN_SHEET_TH = 155;

	private static final String GHOST_SHEET_PATH = "/textures/ghost_normal.png";
	private static final int GHOST_SHEET_TW = 102;
	private static final int GHOST_SHEET_TH = 146;

	private static final String KEY_SHEET_PATH = "/textures/key.png";
	private static final int KEY_SHEET_TW = 70;
	private static final int KEY_SHEET_TH = 70;
	
	private static final String DOOR_SHEET_PATH = "/textures/door.png";
	private static final int DOOR_SHEET_TW = 70;
	private static final int DOOR_SHEET_TH = 110;

	private static final int   MENU_BACKGROUND_SCALE  = 2;
	private static final float MENU_BLUR_RADIUS       = 5.0f;
	
	private TileSheet worldTileSheet;
	private Texture[] worldBackgrounds;

	private TileSheet[] playerIdleTileSheet;
	private TileSheet[] playerBlinkTileSheet;
	private TileSheet[] playerJumpTileSheet;
	private TileSheet[] playerRunTileSheet;
	
	private TileSheet ghostTileSheet;
	private TileSheet keyTileSheet;
	private TileSheet doorTileSheet;
	
	private Texture menuBackground;

	public TextureLoader() {
	}

	public void loadTextures() throws IOException {
		worldTileSheet = readTileSheet(WORLD_SHEET_PATH, WORLD_SHEET_TW, WORLD_SHEET_TH);
		worldBackgrounds = new Texture[NUM_WORLD_BACKGROUNDS];
		for (int i = 0; i < NUM_WORLD_BACKGROUNDS; i++)
			worldBackgrounds[i] = readTexture(String.format(WORLD_BACKGROUNDS_PATH, i));

		playerIdleTileSheet = readPlayerTileSheets(PLAYER_IDLE_SHEET_PATH, PLAYER_IDLE_SHEET_TW, PLAYER_IDLE_SHEET_TH);
		playerBlinkTileSheet = readPlayerTileSheets(PLAYER_BLINK_SHEET_PATH, PLAYER_BLINK_SHEET_TW, PLAYER_BLINK_SHEET_TH);
		playerJumpTileSheet = readPlayerTileSheets(PLAYER_JUMP_SHEET_PATH, PLAYER_JUMP_SHEET_TW, PLAYER_JUMP_SHEET_TH);
		playerRunTileSheet = readPlayerTileSheets(PLAYER_RUN_SHEET_PATH, PLAYER_RUN_SHEET_TW, PLAYER_RUN_SHEET_TH);

		ghostTileSheet = readTileSheet(GHOST_SHEET_PATH, GHOST_SHEET_TW, GHOST_SHEET_TH);
		keyTileSheet = readTileSheet(KEY_SHEET_PATH, KEY_SHEET_TW, KEY_SHEET_TH);
		
		doorTileSheet = readTileSheet(DOOR_SHEET_PATH, DOOR_SHEET_TW, DOOR_SHEET_TH);
		
		Texture composedWorldBackground = readTexture(COMPOSED_WORLD_BACKGROUND_PATH);
		menuBackground = blurTexture(composedWorldBackground, MENU_BACKGROUND_SCALE, MENU_BLUR_RADIUS);
	}
	
	private Texture blurTexture(Texture original, int scale, float radius) {
		int width = original.getWidth() / scale;
		int height = original.getHeight() / scale;
		Image si = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		
		BufferedImage bi;
		if (si instanceof BufferedImage) {
			bi = (BufferedImage)si;
		} else {
			bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = bi.getGraphics();
			g.drawImage(si, 0, 0, null);
			g.dispose();
		}
		
		int[] pixels = ((DataBufferInt)bi.getRaster().getDataBuffer()).getData();
		FastGaussianBlurPixelFilter.filterPixels(radius, pixels, width, height);
		
		return new Texture(bi);
	}
	
	private TileSheet[] readPlayerTileSheets(String basePath, int tileWidth, int tileHeight) throws IOException {
		TileSheet[] tileSheets = new TileSheet[PlayerColor.values().length];
		for (PlayerColor color : PlayerColor.values()) {
			String path = String.format(basePath, color.getName());
			tileSheets[color.getIndex()] = readTileSheet(path, tileWidth, tileHeight);
		}
		return tileSheets;
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

	public Texture[] getWorldBackgrounds() {
		return worldBackgrounds;
	}

	public TileSheet getPlayerIdleTileSheet(PlayerColor color) {
		return playerIdleTileSheet[color.getIndex()];
	}

	public TileSheet getPlayerBlinkTileSheet(PlayerColor color) {
		return playerBlinkTileSheet[color.getIndex()];
	}

	public TileSheet getPlayerJumpTileSheet(PlayerColor color) {
		return playerJumpTileSheet[color.getIndex()];
	}

	public TileSheet getPlayerRunTileSheet(PlayerColor color) {
		return playerRunTileSheet[color.getIndex()];
	}

	public TileSheet getGhostTileSheet() {
		return ghostTileSheet;
	}

	public TileSheet getKeyTileSheet() {
		return keyTileSheet;
	}

	public TileSheet getDoorTileSheet() {
		return doorTileSheet;
	}
	
	public Texture getMenuBackground() {
		return menuBackground;
	}
}

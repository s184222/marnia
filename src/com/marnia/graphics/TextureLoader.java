package com.marnia.graphics;

import javax.imageio.ImageIO;
import java.io.IOException;

public class TextureLoader {

	private static final String WORLD_SHEET_PATH = "/textures/normal/tilesheet.png";
	private static final int WORLD_SHEET_TW = 128;
	private static final int WORLD_SHEET_TH = 128;

	private static final String WORLD_BACKGROUND_PATH = "/textures/normal/background.png";

	private TileSheet worldTileSheet;
	private Texture worldBackground;

	public TextureLoader() {
	}

	public void loadTextures() throws IOException {
		worldTileSheet = readTileSheet(WORLD_SHEET_PATH, WORLD_SHEET_TW, WORLD_SHEET_TH);
		worldBackground = readTexture(WORLD_BACKGROUND_PATH);
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
}

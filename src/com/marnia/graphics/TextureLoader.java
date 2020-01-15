package com.marnia.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TextureLoader {

	private static final String WORLD_SHEET_PATH = "/textures/normal/tilesheet.png";
	private static final int WORLD_SHEET_TW = 128;
	private static final int WORLD_SHEET_TH = 128;

	private TileSheet worldTileSheet;

	public TextureLoader() {
	}

	public void loadTextures() throws IOException {
		worldTileSheet = readTileSheet(WORLD_SHEET_PATH, WORLD_SHEET_TW, WORLD_SHEET_TH);
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
}

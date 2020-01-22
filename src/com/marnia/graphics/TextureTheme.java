package com.marnia.graphics;

import java.io.IOException;

import com.marnia.decorations.DecorationType;
import com.marnia.world.WorldTheme;

public class TextureTheme {

	private static final String WORLD_SHEET_PATH = "/textures/%s/tilesheet.png";
	private static final int WORLD_SHEET_TW = 128;
	private static final int WORLD_SHEET_TH = 128;

	private static final String COMPOSED_WORLD_BACKGROUND_PATH = "/textures/%s/background.png";
	private static final String WORLD_BACKGROUNDS_PATH = "/textures/%s/background_%d.png";
	private static final int NUM_WORLD_BACKGROUNDS = 6;
	
	private static final String WORLD_DECORATIONS_PATH = "/textures/%s/decorations/%s.png";
	
	private static final int   MENU_BACKGROUND_SCALE  = 2;
	private static final float MENU_BLUR_RADIUS       = 5.0f;
	
	private final WorldTheme theme;
	
	private TileSheet worldTileSheet;
	private Texture[] worldBackgrounds;
	private Texture[] worldDecorations;
	
	private Texture menuBackground;
	
	public TextureTheme(WorldTheme theme) {
		this.theme = theme;
	}
	
	public void loadTextures() throws IOException {
		String sheetPath = String.format(WORLD_SHEET_PATH, theme.getName());
		worldTileSheet = TextureLoader.readTileSheet(sheetPath, WORLD_SHEET_TW, WORLD_SHEET_TH);
		
		worldBackgrounds = new Texture[NUM_WORLD_BACKGROUNDS];
		for (int i = 0; i < NUM_WORLD_BACKGROUNDS; i++) {
			String path = String.format(WORLD_BACKGROUNDS_PATH, theme.getName(), i);
			worldBackgrounds[i] = TextureLoader.readTexture(path);
		}

		worldDecorations = new Texture[DecorationType.values().length];
		for (DecorationType type : DecorationType.values()) {
			String path = String.format(WORLD_DECORATIONS_PATH, theme.getName(), type.getName());
			worldDecorations[type.getIndex()] = TextureLoader.readTexture(path);
		}
		
		String backgroundPath = String.format(COMPOSED_WORLD_BACKGROUND_PATH, theme.getName());
		Texture composedWorldBackground = TextureLoader.readTexture(backgroundPath);
		menuBackground = TextureLoader.blurTexture(composedWorldBackground, MENU_BACKGROUND_SCALE, MENU_BLUR_RADIUS);
	}
	
	public TileSheet getWorldTileSheet() {
		return worldTileSheet;
	}

	public Texture[] getWorldBackgrounds() {
		return worldBackgrounds;
	}
	
	public Texture getDecorationTexture(DecorationType type) {
		return worldDecorations[type.getIndex()];
	}
	
	public Texture getMenuBackground() {
		return menuBackground;
	}
}

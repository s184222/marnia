package com.marnia.graphics;

import com.g4mesoft.graphic.IRenderer2D;

public class TileSheet {
	
	private Texture texture;
	
	private int tileWidth;
	private int tileHeight;
	
	public TileSheet(Texture texture, int tileWidth, int tileHeight) throws IllegalArgumentException {
		this.texture = texture;
		// TODO: add validation of texture width and height (must be a multiple of tileWidth and tileHeight.
		if(tileWidth % 128 != 0 || tileHeight % 128 != 0) {
			throw new IllegalArgumentException();
		}
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}
	
	public void render(IRenderer2D renderer, int xp, int yp, int w, int h, int sx, int sy) {
		texture.render(renderer, xp, yp , w, h, sx * tileWidth, sy * tileHeight, tileWidth, tileHeight);
	}
	
	public int getWidth() {
		return texture.getWidth() / tileWidth;
	}
	
	public int getHeight() {
		return texture.getHeight() / tileHeight;
	}
	
	public Texture getTileSheet() {
		return this.texture;
	}

}

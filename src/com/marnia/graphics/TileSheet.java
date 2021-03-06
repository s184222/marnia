package com.marnia.graphics;

import com.g4mesoft.graphic.IRenderer2D;

public class TileSheet {
	
	private final Texture texture;
	
	private final int tileWidth;
	private final int tileHeight;
	
	public TileSheet(Texture texture, int tileWidth, int tileHeight) throws IllegalArgumentException {
		if(texture.getWidth() % tileWidth != 0 || texture.getHeight() % tileHeight != 0)
			throw new IllegalArgumentException("Texture size is not a multiple of tile size!");

		this.texture = texture;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}

	public void render(IRenderer2D renderer, int xp, int yp, int w, int h, int sx, int sy) {
		render(renderer, xp, yp, w, h, sx, sy, false, false);
	}

	public void render(IRenderer2D renderer, int xp, int yp, int w, int h, int sx, int sy, boolean flipX) {
		render(renderer, xp, yp, w, h, sx, sy, flipX, false);
	}
	
	public void render(IRenderer2D renderer, int xp, int yp, int w, int h, int sx, int sy, boolean flipX, boolean flipY) {
		texture.render(renderer, xp, yp , w, h, sx * tileWidth, sy * tileHeight, tileWidth, tileHeight, flipX, flipY);
	}
	
	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}
	
	public int getWidth() {
		return texture.getWidth() / tileWidth;
	}
	
	public int getHeight() {
		return texture.getHeight() / tileHeight;
	}
	
	public Texture getTexture() {
		return texture;
	}

}

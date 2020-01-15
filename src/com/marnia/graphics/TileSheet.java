package com.marnia.graphics;

import java.awt.Graphics;
import com.g4mesoft.graphic.IRenderer2D;

public class TileSheet {
	
	private Texture texture;
	private int tileWidth;
	private int tileHeight;
	
	public TileSheet(Texture texture, int width, int height) {
		this.texture = texture;
		this.tileWidth = width;
		this.tileHeight = height;
	}
	
	public void render(IRenderer2D renderer, int sx, int sy, int w, int h, int xs, int ys, int ws, int hs) {
		texture.render(renderer, sx, sy, sx + w, sy + h, sx, sy, ws + xs, hs + ys);
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

package com.marnia.graphics;

import java.awt.Graphics;
import java.awt.Image;

import com.g4mesoft.graphic.IRenderer2D;

public class Texture {
	
	private final Image texture;
	
	public Texture(Image texture) {
		this.texture = texture;
	}
	
	public void render(IRenderer2D renderer, int xp, int yp, int width, int height) {
		Graphics graphics = renderer.getGraphics();
		graphics.drawImage(texture, xp, yp, width, height, null);
	}

	public void render(IRenderer2D renderer, int xp, int yp, int w, int h, int xs, int ys, int ws, int hs) {
		render(renderer, xp, yp, w, h, xs, ys, ws, hs, false, false);
	}

	public void render(IRenderer2D renderer, int xp, int yp, int w, int h, int xs, int ys, int ws, int hs, boolean flipX) {
		render(renderer, xp, yp, w, h, xs, ys, ws, hs, flipX, false);
	}
	
	public void render(IRenderer2D renderer, int xp, int yp, int w, int h, int xs, int ys, int ws, int hs, boolean flipX, boolean flipY) {
		Graphics graphics = renderer.getGraphics();
		
		int sx0 = flipX ? xs + ws : xs;
		int sx1 = flipX ? xs : xs + ws;
		int sy0 = flipY ? ys + hs : ys;
		int sy1 = flipY ? ys : ys + hs;
		
		graphics.drawImage(texture, xp, yp, xp + w, yp + h, sx0, sy0, sx1, sy1, null);
	}
	
	public int getWidth() {
		return texture.getWidth(null);
	}
	
	public int getHeight() {
		return texture.getHeight(null);
	}
	
	public Image getImage() {
		return texture;
	}
}

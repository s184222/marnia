package com.marnia.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.g4mesoft.graphic.IRenderer2D;

public class Texture {
	
	private final BufferedImage texture;
	
	public Texture(BufferedImage texture) {
		this.texture = texture;
	}
	
	public void render(IRenderer2D renderer, int xp, int yp, int width, int height) {
		Graphics graphics = renderer.getGraphics();
		graphics.drawImage(texture, xp, yp, width, height, null);
	}
	
	public void render(IRenderer2D renderer, int xp, int yp, int w, int h, int xs, int ys, int ws, int hs) {
		Graphics graphics = renderer.getGraphics();
		graphics.drawImage(texture, xp, yp, xp + w, yp + h, xs, ys, ws + xs, hs + ys, null);
	}
	
	public int getWidth() {
		return texture.getWidth();
	}
	
	public int getHeight() {
		return texture.getHeight();
	}
	
	public BufferedImage getImage() {
		return texture;
	}
}
